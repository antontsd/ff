JAK.Utils = JAK.ClassMaker.makeClass({
    NAME: "Utils",
    VERSION: "1.1",
    CLASS: "class"
});


JAK.Utils.prototype.addNewEmail = function () {
    var div = document.getElementById('moreemails');
    var p = document.createElement('p');
    p.className = 'row emailLeft';
    var input = document.createElement('input');
    input.type = 'text';
    input.name = 'favourites';
    p.appendChild(input);

    var p1 = document.createElement('p');
    p1.className = 'row emailRight';
    var input1 = document.createElement('input');
    input1.type = 'text';
    input1.name = 'favourites[]';
    p1.appendChild(input1);

    var cleaner = document.createElement('div');
    cleaner.className = 'cleaner';

    div.appendChild(p);
    div.appendChild(p1);
    div.appendChild(cleaner);

    return false;
}

JAK.Utils.prototype.addNewFolder = function () {
    var div = document.getElementById('morefolders');
    var p = document.createElement('p');
    p.className = 'row';
    var input = document.createElement('input');
    input.type = 'text';
    input.name = 'folders';
    p.appendChild(input);
    div.appendChild(p);

    return false;
}


function activateActiveX() {
    if (document.attachEvent && !window.opera) {
        var obj = document.getElementsByTagName('object');
        for (i = 0; i < obj.length; i++) {
            obj[i].outerHTML = obj[i].outerHTML;
        }
        var embed = document.getElementsByTagName('embed');
        for (i = 0; i < embed.length; i++) {
            embed[i].outerHTML = embed[i].outerHTML;
        }
    }
}

var camera = {
    interval: null,
    cameraSrc: null,
    linkSrc: 'https://napoveda.seznam.cz/kamera.jpg',

    init: function () {
        camera.cameraSrc = document.getElementById('cameraSrc');
        camera.cameraSrc.src = camera.linkSrc + "?rnd=" + Math.round(Math.random() * 655360);
        camera.cameraSrc.className = '';
        camera.interval = window.setInterval(camera.change, 3000);

        return false;
    },

    change: function () {
        camera.cameraSrc.src = camera.linkSrc + "?rnd=" + Math.round(Math.random() * 655360);
    }
};


/* skryvani obsahu diplomovych pracich  */
JAK.ShowHidde = JAK.ClassMaker.makeClass({
    NAME: "ShowHidde",
    VERSION: "1.0",
    CLASS: "class"
});

JAK.ShowHidde.prototype.$constructor = function (show) {
    var links = JAK.DOM.getElementsByClass(show);
    for (var a = 0; a < links.length; a++) {
        JAK.Events.addListener(JAK.DOM.getElementsByClass(show)[a], 'click', this, 'change');
    }
}

JAK.ShowHidde.prototype.change = function (e, elm) {
    if (elm.nextSibling.nextSibling.style.display == 'block') {
        elm.nextSibling.nextSibling.style.display = "none";
        elm.style.backgroundPosition = "0 0";
    }
    else {
        elm.nextSibling.nextSibling.style.display = "block";
        elm.style.backgroundPosition = "0 -13px";
    }
    JAK.Events.cancelDef(e);
    JAK.Events.stopEvent(e);
}

/**
 * =====================================================================================================================
 * @class napoveda
 * pro zobrazeni se vola showHelp a predava se slovnikova promenna k danne polozce napovedy
 **/
JAK.Help = JAK.ClassMaker.makeClass({
    NAME: 'JAK.Help',
    VERSION: '1.0',
    CLASS: 'class'
});
/**
 * @constructor
 * Nastavuje zakladni promenne a na onDomReady navesi vybuildeni helpBoxu a umisteni napovednych ikonek
 **/
JAK.Help.prototype.$constructor = function () {
    this._actualHelpIco = '';
    this.visible = false;
    this.helpOptions = {};
    JAK.Events.onDomReady(this, this._makeBubble);
    this.fillBubble = this.fillBubble.bind(this);
    this.close = this.close.bind(this);
    JAK.Events.onDomReady(this, this._placeIco);
}
/**
 * Metoda vytvarejici bublinu s napovednym textem, kterou appendne do body
 **/
JAK.Help.prototype._makeBubble = function () {
    this.bubble = JAK.cel('div', 'helpBubble', 'helpBubble');
    document.body.appendChild(this.bubble);
}
/**
 * Inicializace jednotlivych ikonek pro zobrazovani napovedy
 * Inicializuje se vzdy tam kde ji chceme umistit a prebira object kde key=id elementu a value = slovnikova polozka s napovednym textem
 * @param {object} option elmID : dictionaryText
 **/
JAK.Help.prototype.bubbleInit = function (option) {
    for (var i in option) {
        this.helpOptions[i] = option[i];
    }
}
/**
 * umisteni ikonek do dokumentu k patricnym elementum + naveseni zobrazovacich udalosti
 **/
JAK.Help.prototype._placeIco = function (e, elm) {
    for (var i in this.helpOptions) {
        var helpElm = JAK.gel(i);
        if (helpElm) {
            var helpIco = JAK.cel('img', 'helpico', 'H-' + i);
            helpIco.src = '/frontend/img/helpico.png';
            helpElm.appendChild(helpIco);
            JAK.Events.addListener(helpIco, 'click', this, '_clickBubble');
            JAK.Events.addListener(helpIco, 'mouseover', this, '_startBubble')
            JAK.Events.addListener(helpIco, 'mouseout', this, '_stopBubble');
        } else {
            var helpElms = JAK.DOM.getElementsByClass(i);
            if (helpElms.length > 0) {
                for (var j = 0; j < helpElms.length; j++) {
                    var helpIco = JAK.cel('img', 'helpico', 'H-' + i);
                    helpIco.src = '/frontend/img/helpico.png';
                    helpElms[j].appendChild(helpIco);
                    JAK.Events.addListener(helpIco, 'click', this, '_clickBubble');
                    JAK.Events.addListener(helpIco, 'mouseover', this, '_startBubble')
                    JAK.Events.addListener(helpIco, 'mouseout', this, '_stopBubble');
                }
            }
        }
    }
}
JAK.Help.prototype._clickClose = function (e, elm) {
    var node = JAK.Events.getTarget(e);
    var status = true;
    while (node != document.body) {
        if (node == this.bubble) {
            return false;
            break;
        }
        node = node.parentNode;
    }
    if (status) {
        this.close();
    }
}
/**
 * Spoustec zavirani okynka (timeout)
 **/
JAK.Help.prototype._closeManager = function (e, elm) {
    this._closeTimeout = setTimeout(this.close, 1000);
}
/**
 * Samotne schovavani okynka
 **/
JAK.Help.prototype.close = function (e, elm) {
    this.bubble.style.display = 'none';
    this.bubble.style.visibility = 'hidden';
    this.visible = false;
    if (this.closeClickAction) {
        JAK.Events.removeListener(this.closeClickAction);
        this.closeClickAction = null;
    }
}
/**
 * Zjisteni velikosti okna prohlizece
 **/
JAK.Help.prototype._getWindowSize = function () {
    var bodyWidth = window.innerWidth || document.documentElement.clientWidth;
    var bodyHeight = window.innerHeight || document.documentElement.clientHeight;
    return {'width': bodyWidth, 'height': bodyHeight};
}
/**
 * Nastartovani zobrazeni okynka (timeout)
 **/
JAK.Help.prototype._startBubble = function (e, elm) {
    this._actualHelpIco = elm;
    if (this._closeTimeout) {
        clearTimeout(this._closeTimeout);
        this._closeTimeout = null;
    }
    this._showTimeout = setTimeout(this.fillBubble, 1000);
    /* fillbubble musi byt zbindovana! */
}
/**
 * Nastartovani schovani okynka
 **/
JAK.Help.prototype._stopBubble = function (e, elm) {
    if (!this.closeClickAction) {
        this.closeClickAction = JAK.Events.addListener(window, 'click', this, '_clickClose');
        /* zavirani na kliknuti */
    }
    if (this._showTimeout) {
        clearTimeout(this._showTimeout);
        this._showTimeout = null;
    }
    this._closeManager();
}
/**
 * Kliknuti na ikonku pro zobrazeni okna
 **/
JAK.Help.prototype._clickBubble = function (e, elm) {
    if (this.visible) {
        this.close(e, elm);
    }
    this._actualHelpIco = elm;
    this.fillBubble();
}
/**
 * Naplni bublinu a spusti napozicovani okna
 */
JAK.Help.prototype.fillBubble = function () {
    var hId = this._actualHelpIco.id != '' ? this._actualHelpIco.id.split('H-')[1] : this._actualHelpIco.className.split('H-')[1];
    this.actualHID = this.helpOptions[hId];
    this.bubble.innerHTML = '<p style="text-align:center;"><img src="/frontend/img/throbber.gif" alt="Loading" /></p>';

    var url = '/frontend/js/dict.xml'
    var rq = new JAK.Request(JAK.Request.XML);
    rq.setCallback(this, "_req");
    rq.send(url);

    /*var hId = this._actualHelpIco.id != '' ? this._actualHelpIco.id.split('H-')[1] : this._actualHelpIco.className.split('H-')[1];
     this.bubble.innerHTML = '<p>'+this.helpOptions[hId]+'</p>';
     if(!this.helpOverAct && !this.helpOutAct){
     this.helpOverAct = JAK.Events.addListener(this.bubble,'mouseover',this,'_activeBubble');
     this.helpOutAct = JAK.Events.addListener(this.bubble,'mouseout',this,'_parentFinder');
     }*/
    this._posBubble();
}
JAK.Help.prototype._req = function (xml) {
    var help = xml.getElementsByTagName('fulltextHelp')[0];
    var text = help.getElementsByTagName(this.actualHID)[0].childNodes[0].nodeValue;
    this.bubble.innerHTML = '<p>' + text + '</p>';
    if (!this.helpOverAct && !this.helpOutAct) {
        this.helpOverAct = JAK.Events.addListener(this.bubble, 'mouseover', this, '_activeBubble');
        this.helpOutAct = JAK.Events.addListener(this.bubble, 'mouseout', this, '_parentFinder');
    }
}
/**
 * Hleda sveho rodice - jeslize nejni v bubline pri prechodu mysi tak bubla do body kde konci a schovava bublinu
 **/
JAK.Help.prototype._parentFinder = function (e, elm) {
    if (!this.closeClickAction) {
        this.closeClickAction = JAK.Events.addListener(window, 'click', this, '_clickClose');
        /* zavirani na kliknuti */
    }
    var node = e.relatedTarget || e.toElement;
    while (node != document.body) {
        if (node == this.bubble) {
            return;
        }
        node = node.parentNode;
    }
    this._closeManager();
}
/**
 * Nuluje schovavaci timeout pri prechodu mysi na help okynko
 **/
JAK.Help.prototype._activeBubble = function (e, elm) {
    if (this._closeTimeout) {
        clearTimeout(this._closeTimeout);
        this._closeTimeout = null;
    }
}
/**
 * Zjisteni pozice ikonky
 **/
JAK.Help.prototype._getIcoPos = function () {
    var icoPos = JAK.DOM.getBoxPosition(this._actualHelpIco);
    icoPos['left'] = icoPos['left'] + this._actualHelpIco.offsetWidth;
    icoPos['top'] = icoPos['top'] + this._actualHelpIco.offsetHeight;
    return icoPos;
}
/**
 * Napozicovani bubliny
 **/
JAK.Help.prototype._posBubble = function () {
    this.visible = true;
    clearTimeout(this._showTimeout);
    var icoPos = this._getIcoPos(this._actualHelpIco);
    if (this.visible) {
        var x = icoPos['left']
        var y = icoPos['top'];
        this.bubble.style.display = 'block';
        var bubbleScroll = JAK.DOM.getBoxScroll(this.bubble);
        var winSize = this._getWindowSize();
        var bubblePosY = (y + this.bubble.offsetHeight) - bubbleScroll['y'];
        var bubblePosX = (x + this.bubble.offsetWidth) - bubbleScroll['x'];
        /* napozicovani nad okno */
        if (bubblePosY > winSize['height']) {
            var diffy = bubblePosY - winSize['height'];
            this.bubble.style.top = (y - (diffy + 5)) + 'px';
        } else {
            this.bubble.style.top = (y) + 'px';
        }
        /* napozicovani vedle okna */
        if (bubblePosX > winSize['width']) {
            var diffx = bubblePosX - winSize['width'];
            this.bubble.style.left = (x - (diffx + 25)) + 'px';
        } else {
            this.bubble.style.left = (x) + 'px';
        }
        this.bubble.style.visibility = 'visible';
        /*this.closeClickAction = JAK.Events.addListener(window,'click',this,'_clickClose');*/
        /* zavirani na kliknuti */
    }
}


JAK.ServicesSlide = JAK.ClassMaker.makeClass({
    NAME: "ServicesSlide",
    VERSION: "1.0",
    CLASS: "static"
});


JAK.ServicesSlide.init = function () {
    // nastaveni
    this.prevId = 'servicesPrev';
    this.nextId = 'servicesNext';
    this.slideElmId = 'servicesListView';
    this.servicesPerView = 4;
    this.fps = 100;
    this.pxPerFps = 40;
    // end of nastaveni

    // donastaveni
    this.prev = JAK.gel(this.prevId);
    this.next = JAK.gel(this.nextId);
    this.slideElm = JAK.gel(this.slideElmId);
    this.slideElmInitialLeft = JAK.DOM.getBoxPosition(this.slideElm).left;
    this.actualView = 0;
    this.slideInterval = null;
    this.slideTargetPosition = 0;
    // end of donastaveni

    // zjistim pocet sluzeb celkem a delku jednoho posunu
    var services = JAK.DOM.getElementsByClass('item', this.slideElm, 'div');
    this.servicesCount = JAK.DOM.getElementsByClass('item', this.slideElm, 'div').length;
    this.slideDistance = services[0].offsetWidth * this.servicesPerView;
    // end of zjistim pocet sluzeb celkem a delku posunu

    // zjistim pocet stranek
    this.viewsCount = parseInt(this.servicesCount / this.servicesPerView);
    if (this.servicesCount % this.servicesPerView > 0) {
        this.viewsCount++;
    }
    // end of zjistim pocet stranek

    // bindovani prev/next prvku
    JAK.Events.addListener(this.prev, 'click', this, '_back');
    JAK.Events.addListener(this.next, 'click', this, '_forward');
    // end of bindovani prev/next prvku

    //alert(JAK.Dom.getBoxPosition(this.slideElm, this.slideElm.parentNode).left);
}

JAK.ServicesSlide._start = function () {
    if (this.slideInterval == null) {
        this.slideInterval = setInterval(this._slide.bind(this), parseInt(1000 / this.fps));
    }
}

JAK.ServicesSlide._stop = function () {
    if (this.slideInterval != null) {
        clearInterval(this.slideInterval);
        this.slideInterval = null;
    }

    if (this.actualView == 0) {
        JAK.DOM.addClass(this.prev, 'disabled');
    } else {
        JAK.DOM.removeClass(this.prev, 'disabled');
    }

    if (this.actualView == this.viewsCount - 1) {
        JAK.DOM.addClass(this.next, 'disabled');
    } else {
        JAK.DOM.removeClass(this.next, 'disabled');
    }
}

JAK.ServicesSlide._isSliding = function () {
    if (this.slideInterval == null) {
        return false;
    } else {
        return true;
    }
}

JAK.ServicesSlide._slide = function () {
    var actualLeft = JAK.DOM.getBoxPosition(this.slideElm).left - this.slideElmInitialLeft;

    if (Math.abs(this.slideTargetPosition - actualLeft) <= this.pxPerFps) {
        this.slideElm.style.left = this.slideTargetPosition + 'px';
        this._stop();
        return;
    }

    if (this.slideTargetPosition < actualLeft) {
        this.slideElm.style.left = actualLeft - this.pxPerFps + 'px';
    } else {
        this.slideElm.style.left = actualLeft + this.pxPerFps + 'px';
    }
}

JAK.ServicesSlide._back = function (e, elm) {
    if (!this._isSliding() && this.actualView != 0) {
        this.slideTargetPosition = this.slideTargetPosition + this.slideDistance;
        this.actualView--;
        this._start();
    }
}

JAK.ServicesSlide._forward = function (e, elm) {
    if (!this._isSliding() && this.actualView != this.viewsCount - 1) {
        this.slideTargetPosition = this.slideTargetPosition - this.slideDistance;
        this.actualView++;
        this._start();
    }
}


/**
 * @class ShowSkins
 **/
JAK.ShowSkins = JAK.ClassMaker.makeClass({
    NAME: 'JAK.ShowSkins',
    VERSION: '1.0',
    CLASS: 'class'
});
/**
 * @constructor
 * vykresleni skinu ze skriptu skins.def.js
 * @param {string} cilovy element
 * @param {string} cesta k obrazkum
 * @param {int} vybrane id skinu
 **/
JAK.ShowSkins.prototype.$constructor = function (elm_target, path, skin_number) {
    var target = JAK.gel(elm_target);
    if (!target) return;
    path = path || '';
    var data = [];

    if (typeof SKINS != "undefined") {
        data = SKINS;
    } else if (typeof LAYOUTS != "undefined") {
        data = LAYOUTS;
    }
    for (var i = 0; i < data.length; i++) {
        if (data[i].id == "vve") {
            data[i].id = 1000;
        }
        var skin = JAK.cel("div", "skin", "");
        var label = JAK.cel("label");
        label.setAttribute("for", "skinN" + data[i].id);
        var img = JAK.cel("img");
        img.setAttribute("src", path + data[i].preview);
        img.setAttribute("width", 170);
        img.setAttribute("height", 110);
        img.setAttribute("alt", data[i]['name.cs']);
        var input = JAK.cel("input", "", "skinN" + data[i].id);
        input.setAttribute("type", "radio");
        input.setAttribute("name", "skinNumber");
        input.setAttribute("value", data[i].id);
        if (typeof skin_number != "undefined" && skin_number == data[i].id) {
            input.setAttribute("checked", 1);
        }
        var span = JAK.cel("span");
        span.innerHTML = data[i]['name.cs'];

        JAK.DOM.append([target, skin], [skin, label], [label, img, input, span]);
    }
    return;
}

/**
 * @class HelpfulArticle
 **/
JAK.HelpfulArticle = JAK.ClassMaker.makeClass({
    NAME: 'JAK.HelpfulArticle',
    VERSION: '1.0'
});

JAK.HelpfulArticle.prototype.$constructor = function (params) {

    this.helpfulDom = JAK.query('div.helpful')[0];

    if (!this.helpfulDom) {
        return;
    }

    if (params.helpfulEnabled) {
        this.pageId = params.pageId;

        // Zobrazi element helpful
        this.helpfulDom.style.display = 'block';

        // Odkazy
        var yesNoLinks = JAK.query('div.helpful a', this.pageId);
        this.yesLink = yesNoLinks[0];
        this.noLink = yesNoLinks[1];

        // Naveseni udalosti na odkazy
        JAK.Events.addListener(this.yesLink, 'click', this, '_yesClicked');
        JAK.Events.addListener(this.noLink, 'click', this, '_noClicked');
    } else {
        if (params.thanksVoted) {
            this._thanksVoted();
        }
    }
}

/**
 * Vypsani hlasky "Děkujeme za Váš názor."
 */
JAK.HelpfulArticle.prototype._thanksVoted = function () {
    this.helpfulDom.style.display = 'block';
    this.helpfulDom.innerHTML = '<span class="item">Děkujeme za Váš názor.</span>';
}

/**
 * Kliknuti na ANO
 */
JAK.HelpfulArticle.prototype._yesClicked = function (e, elm) {
    JAK.Events.cancelDef(e);
    this._sendVote('?quality=positiveCount&id=' + this.pageId);
}

/**
 * Kliknuti na NE
 */
JAK.HelpfulArticle.prototype._noClicked = function (e, elm) {
    JAK.Events.cancelDef(e);
    this._sendVote('?quality=negativeCount&id=' + this.pageId);
}

/**
 * Odesle AJAXem info o tom zda clanek uzivateli pomohl
 */
JAK.HelpfulArticle.prototype._sendVote = function (url) {
    var rq = new JAK.Request(JAK.Request.TEXT);
    rq.setCallback(this, '_response');
    rq.send(location.href + url);

    this._thanksVoted();
}

/**
 * Callback AJAX odpovedi
 */
JAK.HelpfulArticle.prototype._response = function (data, status) {

}

/**
 * Na ipadu a ifounu prskne místo flash playeru html5 video
 */
JAK.Html5Video = JAK.ClassMaker.makeClass({
    NAME: "Html5Video",
    VERSION: "1.0",
    CLASS: "class"
});

JAK.Html5Video.prototype.$constructor = function (videoParams, videoContId) {
    if (JAK.Browser.agent.indexOf("iPad") != -1 || JAK.Browser.agent.indexOf("iPhone") != -1) {
        var player = JAK.DOM.getElementsByClass(videoContId);
        for (var i = 0; i < videoParams.length; i++) {
            var video = JAK.cel("video");
            video.poster = videoParams[i].image;
            video.src = videoParams[i].video;
            video.controls = true;
            if (i > 0) {
                video.width = 568;
                video.height = 359;
            } else {
                video.width = videoParams[i].width;
                video.height = videoParams[i].height;
            }
            player[i].innerHTML = '';
            player[i].appendChild(video);
        }
    }
}


/**
 * zobrazovani captchy po focusu do textarey/inputu,
 * pouziva se na kontaktnim formulari pro novy sbazar
 */
JAK.ShowCaptcha = JAK.ClassMaker.makeClass({
    NAME: "ShowCaptcha",
    VERSION: "1.0",
    CLASS: "class"
});

JAK.ShowCaptcha.prototype.$constructor = function (captchaWrap, activateInput) {
    this._captchaWrap = JAK.gel(captchaWrap);
    this._activateInput = JAK.gel(activateInput);

    if (!this._captchaWrap || !this._activateInput) {
        return;
    }

    //zapamatovat rozmery
    this._height = this._captchaWrap.offsetHeight;

    //schovat
    this._captchaWrap.style.display = 'none';

    JAK.Events.addListener(this._activateInput, 'focus', this, '_ev_focus');
}

JAK.ShowCaptcha.prototype._ev_focus = function (e, elm, eventId) {
    JAK.Events.removeListener(eventId);
    this._showStart();
}

JAK.ShowCaptcha.prototype._showStart = function () {
    this._captchaWrap.style.overflow = 'hidden';
    this._captchaWrap.style.height = '0px';
    this._captchaWrap.style.display = '';

    this._actH = 0;
    this._diffH = 25;
    this._diffT = 30;

    this._interval = setInterval(this._showEnd.bind(this), this._diffT);
}

JAK.ShowCaptcha.prototype._showEnd = function () {
    var newH = this._actH + this._diffH;
    if (newH > this._height) {
        clearInterval(this._interval);
        this._captchaWrap.style.overflow = '';
        this._captchaWrap.style.height = '';
    } else {
        this._captchaWrap.style.height = newH + 'px';
        this._actH = newH;
    }
}



