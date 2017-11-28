/* otevirani - zavirani divu */
JAK.openClose = JAK.ClassMaker.makeClass({
    NAME: "openClose",
    VERSION: "1.0",
    CLASS: "class"
});


JAK.openClose.prototype.$constructor = function (className, closeClassName, tagName, tagClass, htmlOpener, htmlCloser) {
    var objs = JAK.DOM.getElementsByClass(className);
    for (var i = 0; i < objs.length; i++) {
        var parentObj;
        var opener;
        var insert;
        var box;

        box = JAK.cel('div');
        cleaner = JAK.cel('div', '', 'clean cleaner');
        opener = JAK.cel(tagName, '', tagClass);

        if (tagName == 'a') opener.href = "#";
        opener.innerHTML = htmlOpener;
        opener._obj = objs[i];

        opener._objCloseText = htmlCloser;
        opener._objOpenText = htmlOpener;

        opener._objCloseClass = objs[i].className + ' ' + closeClassName;
        opener._objOpenClass = objs[i].className;
        objs[i].className = opener._objCloseClass;

        JAK.Events.addListener(opener, 'click', this, 'onClick');

        parentObj = objs[i].parentNode;
        objs[i].appendChild(box);
        parentObj.replaceChild(box, objs[i]);

        box.appendChild(objs[i]);
        box.appendChild(opener);
        box.appendChild(cleaner);
    }
}

JAK.openClose.prototype.onClick = function (e, elm) {
    if (e) JAK.Events.cancelDef(e);
    if (elm._obj.className == elm._objCloseClass) {
        elm._obj.className = elm._objOpenClass;
        elm.innerHTML = elm._objCloseText;
    } else {
        elm._obj.className = elm._objCloseClass;
        elm.innerHTML = elm._objOpenText;
    }
}

