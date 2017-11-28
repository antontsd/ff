/**
 * @overview Suggest NG - naseptavac nove generace
 * @version 4.1
 * @author zara
 */

/**
 * @class Trida, poskytujici "naseptavac" z dane adresy pro zadany inputbox
 * @signal suggestSubmit
 */
JAK.Suggest = JAK.ClassMaker.makeClass({
    NAME: "JAK.Suggest",
    VERSION: "4.1",
    IMPLEMENT: JAK.ISignals
});

/**
 * @constant
 * Automaticky rezim, kdy se hodnota inputu meni vybiranim polozek
 */
JAK.Suggest.MODE_AUTOMATIC = 0;
/**
 * @constant
 * Manualni rezim, kdy se hodnota inputu meni sipkou doprava
 */
JAK.Suggest.MODE_MANUAL = 1;

/**
 * @param {string} id ID inputboxu, na ktery bude suggest navesen
 * @param {string} url adresa, na kterou se provadi zadost; musi byt v domene dokumentu!
 * @param {object} [options] Hash s dodatecnymi opsnami
 * @param {string} [options.dict=""] nazev slovniku
 * @param {int} [options.count=10] pocet vysledku
 * @param {bool} [options.prefix=false] ma-li se pouzit prefixove hledani
 * @param {bool} [options.highlight=false] ma-li se ve vysledcich zvyraznit hledany (pod)retezec
 * @param {bool} [options.mode=JAK.Suggest.MODE_AUTOMATIC] jak se ma chovat input pri vyberu polozek
 * @param {bool} [options.itemCtor=JAK.Suggest.Item] kostruktor slovnikove polozky
 * @param {bool} [options.timeout=100] jak dlouho po zastaveni psani poslat request
 * @param {bool} [options.parentElement=id.form] rodic
 */
JAK.Suggest.prototype.$constructor = function (id, url, options) {
    this.ec = [];
    /* event cache */
    this.dom = {};
    this.url = url;
    this.options = {
        dict: "",
        count: 10,
        prefix: false,
        highlight: false,
        mode: JAK.Suggest.MODE_AUTOMATIC,
        itemCtor: JAK.Suggest.Item,
        parentElement: JAK.gel(id).form,
        timeout: 200
    }

    for (var p in options) {
        this.options[p] = options[p];
    }
    this.items = [];
    this.remotes = [];

    this._rq = null;
    this._timeout = false;
    this._activeItem = false;
    this._request = this._request.bind(this);
    this._build(id);
    this._hoverLock = false;
    this._used = 0;
    /* 0 - not used, 1 - used partially, 2 - used fully */
}

JAK.Suggest.prototype.$destructor = function () {
    this._clear();
    this.ec.forEach(JAK.Events.removeListener, JAK.Events);
}

/**
 * Za behu zmeni opsny
 */
JAK.Suggest.prototype.setOptions = function (options) {
    for (var p in options) {
        this.options[p] = options[p];
    }
}

/**
 * Posle XMLHttpRequest
 */
JAK.Suggest.prototype._request = function () {
    if (this.dom.input.value.length > 0 && this.dom.input.value.trim().length > 0) {
        this._timeout = false;
        var url = this._buildUrl(this.dom.input.value);

        /* pokud request jiz bezi, zabijeme ho */
        if (this._rq) {
            this._rq.abort();
        }

        this._rq = new JAK.Request(JAK.Request.XML);
        this._rq.setCallback(this, "_response");
        this._rq.send(url);
    }
}

/**
 * Zavola se pri "aktivaci" vybrane polozky (kliknuti, enter). Defaultne submitne formular.
 */
JAK.Suggest.prototype.action = function () {
    if (this._activeItem) {
        this.dom.input.value = this._activeItem.getValue();
    }
    this._hide();
    this.makeEvent("suggestSubmit", {form: this.options.parentElement});
    this._used = 0;
    this.options.parentElement.submit();
}

/**
 * Vrati cislo, popisujici, jak moc byl suggest pouzit (0..2)
 */
JAK.Suggest.prototype.used = function () {
    return this._used;
}

/**
 * Vrati prave vybranou polozku (instance JAK.Suggest.Remote/JAK.Suggest.Item)
 */
JAK.Suggest.prototype.getActive = function () {
    return this._activeItem;
}

JAK.Suggest.prototype._build = function (id) {
    var input = JAK.gel(id);

    var container = JAK.mel("div", {className: "suggest"}, {width: input.offsetWidth + "px"});
    this.options.parentElement.appendChild(container);

    var content = JAK.mel("div", {className: "content"});
    container.appendChild(content);

    this.dom.input = input;
    this.dom.container = container;
    this.dom.content = content;

    this._hide();
    this.options.parentElement.appendChild(container);

    this.ec.push(JAK.Events.addListener(input, "keyup", this, "_keyUp"));
    this.ec.push(JAK.Events.addListener(input, "keydown", this, "_keyNavigate"));

    this.ec.push(JAK.Events.addListener(container, "mousedown", JAK.Events.stopEvent));
    this.ec.push(JAK.Events.addListener(document, "mousedown", this, "_hide"));
    this.ec.push(JAK.Events.addListener(document, "mousemove", this, "_unlock"));
}

JAK.Suggest.prototype._show = function () {
    this._hoverLock = true;
    this.dom.container.style.display = "block";
}

JAK.Suggest.prototype._hide = function () {
    this.dom.container.style.display = "none";
}

JAK.Suggest.prototype._clear = function () {
    JAK.DOM.clear(this.dom.content);
    for (var i = 0; i < this.items.length; i++) {
        this.items[i].$destructor();
    }
    for (var i = 0; i < this.remotes.length; i++) {
        this.remotes[i].$destructor();
    }
    this.items = [];
    this.remotes = [];
    this._activeItem = false;
}

JAK.Suggest.prototype._response = function (xmlDoc) {
    this._rq = null;
    this._clear();
    var result = xmlDoc.documentElement;
    var items = result.getElementsByTagName("item");
    for (var i = 0; i < items.length; i++) {
        var item = items[i];
        if (item.parentNode.parentNode.nodeName.toLowerCase() == "remote") {
            this._buildItem(JAK.Suggest.Remote, item, this.remotes);
        } else {
            this._buildItem(this.options.itemCtor, item, this.items);
        }
    }

    for (var i = 0; i < this.items.length; i++) {
        this.dom.content.appendChild(this.items[i].getContainer());
    }
    for (var i = 0; i < this.remotes.length; i++) {
        this.dom.content.appendChild(this.remotes[i].getContainer());
    }

    if (this.items.length + this.remotes.length) {
        this._show();
    } else {
        this._hide();
    }
}

JAK.Suggest.prototype._buildUrl = function (query) {
    var url = this.url;
    if (url.charAt(url.length - 1) != "/") {
        url += "/";
    }
    url += this.options.dict;
    var arr = [];
    arr.push("phrase=" + encodeURIComponent(query));
    arr.push("result=xml");
    if (this.options.prefix) {
        arr.push("prefix=1");
    }
    if (this.options.highlight) {
        arr.push("highlight=1");
    }
    if (this.options.count) {
        arr.push("count=" + this.options.count);
    }

    url += "?" + arr.join("&");
    return url;
}

JAK.Suggest.prototype._buildItem = function (constructor, node, arr) {
    var item = new constructor(this, node);
    arr.push(item);
}

JAK.Suggest.prototype._activate = function (item, activate) {
    this._activeItem = item;
    for (var i = 0; i < this.items.length; i++) {
        var it = this.items[i];
        if (it == item) {
            it.hoverOn();
        } else {
            it.hoverOff();
        }
    }

    if (activate && this.options.mode == JAK.Suggest.MODE_AUTOMATIC) {
        this.dom.input.value = this._activeItem.getValue();
    }
}

JAK.Suggest.prototype._keyNavigate = function (e, elm) {
    var code = e.keyCode;

    if (code == 13) {
        if (this._used == 1 && this._activeItem) {
            this._used = 2;
        }
        /* odesilame po probehle navigaci a mame aktivni prvek */
        this.action();
        return;
    }

    if (code == 39 && this._activeItem && this.options.mode == JAK.Suggest.MODE_MANUAL) { /* sipka doprava */
        this._used = 2;
        this.dom.input.value = this._activeItem.getValue();
        this._request();
    }

    if (this.items.length && code == 38) { /* sipka nahoru */
        this._used = 1;
        var index = this.items.indexOf(this._activeItem);
        index = (index == -1 || index == 0 ? 0 : index - 1);
        this._activate(this.items[index], true);
    }

    if (this.items.length && code == 40) { /* sipka dolu */
        this._used = 1;
        var index = this.items.indexOf(this._activeItem);
        var cnt = this.items.length;
        if (index == -1) {
            index = 0;
        } else if (index + 1 == cnt) {
            index = cnt - 1;
        } else {
            index++;
        }
        this._activate(this.items[index], true);
    }

    if (code == 27 || code == 9) {
        this._hide();
    }
    /* esc */
}

JAK.Suggest.prototype._keyUp = function (e, elm) {
    var code = e.keyCode;
    if (code == 8 || code == 46) { /* backspace, delete */
        this._activate(false);
        this._startRequest();
    } else if (((code < 33) || (code > 39)) && (code != 13) && (code != 27) && (code != 40) && (code != 44) && (code != 45) && (code != 17) && (code != 18)) {
        this._startRequest();
    }
}

JAK.Suggest.prototype._startRequest = function () {
    if (this._timeout) {
        clearTimeout(this._timeout);
    }
    this._timeout = setTimeout(this._request, this.options.timeout);
}

JAK.Suggest.prototype._unlock = function () {
    this._hoverLock = false;
}

/***/

JAK.Suggest.Remote = JAK.ClassMaker.makeClass({
    NAME: "JAK.Suggest.Remote",
    VERSION: "2.0"
});

JAK.Suggest.Remote.prototype.$constructor = function (owner, node) {
    this.owner = owner;
    this.dom = {};
    this.ec = [];

    this.node = node;
    this.value = node.getAttribute("value");

    this._build();
}

JAK.Suggest.Remote.prototype.$destructor = function () {
    this.ec.forEach(JAK.Events.removeListener, JAK.Events);
}

JAK.Suggest.Remote.prototype.getContainer = function () {
    return this.dom.container;
}

/**
 * Hodnota teto polozky
 * @returns {string}
 */
JAK.Suggest.Remote.prototype.getValue = function () {
    return this.value;
}

/**
 * XML uzel patrici teto polozce
 * @returns {node}
 */
JAK.Suggest.Remote.prototype.getNode = function () {
    return this.node;
}

JAK.Suggest.Remote.prototype._build = function () {
    var p = JAK.mel("p", {className: "remote"});
    p.innerHTML = this.value;

    var span = this._buildRelevance();
    p.insertBefore(span, p.firstChild);

    this.dom.container = p;
}

JAK.Suggest.Remote.prototype._buildRelevance = function () {
    var span = JAK.mel("span", {className: "relevance"});
    span.innerHTML = "rychlý tip";
    return span;
}

/***/

JAK.Suggest.Item = JAK.ClassMaker.makeClass({
    NAME: "JAK.Suggest.Item",
    VERSION: "2.0",
    EXTEND: JAK.Suggest.Remote
});

JAK.Suggest.Item.prototype._build = function () {
    var p = JAK.mel("p", {className: "item"});

    p.innerHTML = this.value;

    var span = this._buildRelevance();
    p.insertBefore(span, p.firstChild);

    this.dom.container = p;

    this.ec.push(JAK.Events.addListener(this.dom.container, "click", this, "_action"));
    this.ec.push(JAK.Events.addListener(this.dom.container, "mouseover", this, "_over"));
}

JAK.Suggest.Item.prototype._buildRelevance = function () {
    var span = JAK.mel("span", {className: "relevance"});
    span.innerHTML = this.node.getAttribute("relevance");
    return span;
}

JAK.Suggest.Item.prototype._action = function (e, elm) {
    this.owner._used = 2;
    /* klik mysi = plnohodnotne vyuziti suggestu */
    this.owner.action();
}

JAK.Suggest.Item.prototype._over = function () {
    if (this.owner._hoverLock) {
        return;
    }
    this.owner._activate(this, false);
}

JAK.Suggest.Item.prototype.hoverOn = function () {
    JAK.DOM.addClass(this.dom.container, "active");
}

JAK.Suggest.Item.prototype.hoverOff = function () {
    JAK.DOM.removeClass(this.dom.container, "active");
}

