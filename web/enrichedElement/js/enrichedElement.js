//global variables
var keys = [false, false, false, false, false, false, false, false];//[shift, 5, °, -, +,x,y,z]
var enrichedElements = new Array();
var currentFocus, previousFocus;
var option = -1; //-1 = no option, 0 = sub, 1 = super
var mouseInHelp = false;
//append caret script
appendCaretScript();
//search for enriched elements on the page
findAllEnrichedElements();
//make enriched content editable + add onfocus listener
makeEnrichedReady();
//make empty div for menuHelper
prepareHelper();

//-----------------------------------------------------------------------------------------------
function refreshEnriched() {
    findAllEnrichedElements();
    makeEnrichedReady();
}

function appendCaretScript() {
    var body = $(document.body);
    body.append("<script type=\"text/javascript\" src=\"enrichedElement/js/caret.js\"></script>");
}

function findAllEnrichedElements() {
    enrichedElements = new Array();
    var body = $(document.body);
    var allElements = body.find("*");
    for (var i = 0; i < allElements.length; i++) {
        var className = allElements[i].className;
        if (/enriched/i.test(className)) {
            enrichedElements.push(allElements[i]);
        }
    }
}

function makeEnrichedReady() {
    for (var i = 0; i < enrichedElements.length; i++) {
        enrichedElements[i].setAttribute("contentEditable", "true");
        enrichedElements[i].addEventListener("focus", elementFocussed);
        enrichedElements[i].addEventListener("focusout", elementUnfocussed);
        $(enrichedElements[i]).css('min-width', '1em');
        $(enrichedElements[i]).css('-webkit-user-modify', 'read-write');
    }
}

function prepareHelper() {
    var body = $(document.body);
    body.append("<div class=\"helpBox\"></div>");
    //load
    $('.helpBox').load("enrichedElement/fieldMenu.html #menu", onLoadComplete); //help menu for sub- & superscript	
    $('.helpBox').draggable();
    $('.helpBox').hide();
}

function onLoadComplete(responseText, textStatus, jqXHR) {
    if (textStatus === 'error')
        retryLoadFromPartial();
    //add EventListeners - two id's from fieldMenu.html
    $('#sub').click(function () {
        if (option !== 0) {//normal or super
            option = 0;
            $('#sub').css("background-color", "#eedfcc");
            $('#sup').css("background-color", "#fffaf0");
        } else {
            option = -1;
            $('#sub').css("background-color", "#fffaf0");
        }
    });
    $("#sup").click(function () {
        if (option !== 1) {
            option = 1;
            $('#sup').css("background-color", "#eedfcc");
            $('#sub').css("background-color", "#fffaf0");
        } else {
            option = -1;
            $('#sup').css("background-color", "#fffaf0");
        }
    });
    $('.helpBox').mouseenter(function () {
        mouseInHelp = true;
    });
    $('.helpBox').mouseleave(function () {
        mouseInHelp = false;
    });
}

function retryLoadFromPartial() {
    $('.helpBox').load("../enrichedElement/fieldMenu.html #menu", onLoadComplete); //help menu for sub- & superscript	
    $('.helpBox').draggable();
    $('.helpBox').hide();
}

function elementFocussed() {//handles the event if the element is focussed    
    previousFocus = currentFocus;
    currentFocus = document.activeElement;
    currentFocus.addEventListener("keydown", keyPressed);
    currentFocus.addEventListener("keyup", keyReleased);
    //remove placeholder
    if (currentFocus.innerHTML === "Hier typen") {
        currentFocus.innerHTML = "";
    }
    //show help bar    
    showHelpBar();

}

function elementUnfocussed() {//reset keyListeners
    currentFocus.removeEventListener("keydown", keyPressed);
    currentFocus.removeEventListener("keyup", keyReleased);
    if (!mouseInHelp) {//user leaves the current field
        currentFocus = null;
        hideHelpBar();
        //forget selected option
        option = -1;
        $('#sup').css("background-color", "#fffaf0");
        $('#sub').css("background-color", "#fffaf0");
    }
}

function showHelpBar() {
    if (previousFocus === currentFocus) {//focusses the same field
        return;//already visible do nothing
    }
    $('.helpBox').hide();
    var x = (currentFocus.getBoundingClientRect().right / 2) - $('.helpBox').width();
    var y = currentFocus.getBoundingClientRect().top - $('.helpBox').height();
    $('.helpBox').css({top: y, left: x, position: 'absolute'});
    $('.helpBox').css("padding-left", "50px");
    $('.helpBox').show(500, "easeInOutCubic");
}

function hideHelpBar() {
    $('.helpBox').hide(250, "easeInOutCubic");
}

function keyReleased(event) {
    if (event.keyCode === 16) {
        //shift
        keys[0] = false;
        return false
    }
    if (event.keyCode === 53) {//5
        keys[1] = false;
    }
    if (event.keyCode === 169) {//)
        keys[2] = false;
    }
    if (event.keyCode === 173) {//-
        keys[3] = false;
    }
    if (event.keyCode === 61) {//+
        keys[4] = false;
    }
    if (event.keyCode === 88) {//x
        keys[5] = false;
    }
    if (event.keyCode === 89) {//y
        keys[6] = false;
    }
    if (event.keyCode === 90) {//z
        keys[7] = false;
    }
}

function keyPressed(event) {
    if (event.keyCode === 16) {
        //shift
        keys[0] = true;
        return false;
    }
    if (event.keyCode === 53) {//5
        keys[1] = true;
    }
    if (event.keyCode === 169) {//°
        keys[2] = true;
    }
    if (event.keyCode === 173) {//-
        keys[3] = true;
    }
    if (event.keyCode === 61) {//+
        keys[4] = true;
    }
    if (event.keyCode === 88) {//x
        keys[5] = true;
    }
    if (event.keyCode === 89) {//y
        keys[6] = true;
    }
    if (event.keyCode === 90) {//z
        keys[7] = true;
    }
    if (option !== -1) {
        processEnrichedKey(event);
    } else {//user wants to type inline
        processNormalKey(event);
    }
}

function processNormalKey(event) {
    var character = codeToCharacter(event.keyCode);
    if (/Û/.test(character)) {
        character = ")";
    }


    if (/[0-9a-zA-Z]|\s|\(|\)/.test(character)) {
        var oldInsert = getCaretPosition();

        var correctInsertPoint = updatedInsertPoint(getCaretPosition());

        var parts = new Array();

        parts[0] = (currentFocus.innerHTML.substring(0, correctInsertPoint));

        parts[1] = (currentFocus.innerHTML.substring(correctInsertPoint));
        character = (isCapslock(event)) ? ("" + character).toUpperCase() : ("" + character).toLowerCase();
        var newValue = parts[0] + character + parts[1];
        event.preventDefault();
        updateNormalKeyStroke(newValue);
        determineNewCaretPosition(oldInsert, parts[1] == 0);

        return false;
    }
}

function isCapslock(e) {
    //source http://stackoverflow.com/questions/348792/how-do-you-tell-if-caps-lock-is-on-using-javascript
    e = (e) ? e : window.event;

    var charCode = false;
    if (e.which) {
        charCode = e.which;
    } else if (e.keyCode) {
        charCode = e.keyCode;
    }

    var shifton = false;
    if (e.shiftKey) {
        shifton = e.shiftKey;
    } else if (e.modifiers) {
        shifton = !!(e.modifiers & 4);
    }

    if (charCode >= 97 && charCode <= 122 && shifton) {
        return false;
    }

    if (charCode >= 65 && charCode <= 90 && !shifton) {
        return false;
    }

    return true;

}

function updateNormalKeyStroke(newValue) {
    currentFocus.innerHTML = newValue;
}

function processEnrichedKey(event) {
    var before = currentFocus.innerHTML;
    var code = event.keyCode;
    if (isValid(code) && before.length !== 0) {//only allow numbers prevent sub or superscript when field is empty    
        var character = codeToCharacter(code);
        var newhtml;
        if (option === 0) {
            newhtml = makeSubscript(character);
        } else if (option === 1) {
            newhtml = makeSuperscript(character);
        }
        event.preventDefault();
        updateField(getCaretPosition(), before, newhtml);
        return false;
    } else {
        if (currentFocus != null) {//jquery            
            $(currentFocus).effect("shake");
        }
    }
}

function getCaretPosition() {
    return $(currentFocus).caret();
}

function isValid(code) {
    if ((code >= 48 && code <= 57) || (code >= 96 && code <= 105)) {//number
        return true;
    }
    if (code === 16) {//shift
        return true;
    }
    if (code === 169) {
        return true;
    }
    if (code === 107 || code === 109) {//numpad + and -
        return true;
    }
    if (code === 173) {
        return true;
    }
    if (code === 61) {
        return true;
    }
    if (code === 88) {
        return true;
    }
    if (code === 89) {
        return true;
    }
    if (code === 90) {
        return true;
    }
    return false;
}

function makeSubscript(character) {
    if (character.length === 1 || typeof (character) === "number") {
        return "<sub>" + character + "</sub>";
    }
}

function makeSuperscript(character) {
    if (character.length === 1 || typeof (character) === "number") {
        return "<sup>" + character + "</sup>";
    }
}

function codeToCharacter(code) {
    var character = String.fromCharCode(code);
    if (keys[0] && keys[1]) {//shift + 5
        return 5;
    }
    if (!keys[0] && keys[1]) {//only 5 pressed, so (
        return '(';
    }
    if (!keys[0] && keys[2]) {//° ( = ')' ) pressed
        return ')';
    }
    if (keys[0] && keys[2]) {//shift + ° ( = ')' ) pressed, om verwarring te voorkomen
        return ')';
    }
    if (keys[0] && keys[3]) {//shift + - ( = '-' ) pressed
        return '-';
    }
    if (!keys[0] && keys[3]) {//shift + - ( = '-' ) pressed
        return '-';
    }
    if (keys[0] && keys[4]) {//shift + '+' ( = '+' ) pressed
        return '+';
    }
    if (!keys[0] && keys[4]) {//shift + '+' ( = '+' ) pressed
        return '+';
    }
    if (!keys[0] && keys[5]) {//shift + '+' ( = '+' ) pressed
        return 'x';
    }
    if (!keys[0] && keys[6]) {//shift + '+' ( = '+' ) pressed
        return 'y';
    }
    if (!keys[0] && keys[7]) {//shift + '+' ( = '+' ) pressed
        return 'z';
    }
    if (keys[0] && keys[5]) {//shift + '+' ( = '+' ) pressed
        return 'X';
    }
    if (keys[0] && keys[6]) {//shift + '+' ( = '+' ) pressed
        return 'Y';
    }
    if (keys[0] && keys[7]) {//shift + '+' ( = '+' ) pressed
        return 'Z';
    }
    if (code >= 96 && code <= 105 || code === 107 || code === 109) {
        character = processNumpad(code);
    }
    if (code === 160) {
        return "[";
    }
    return character;
}

function processNumpad(code) {
    var character;
    switch (code) {
        case 96:
            character = 0;
            break;
        case 97:
            character = 1;
            break;
        case 98:
            character = 2;
            break;
        case 99:
            character = 3;
            break;
        case 100:
            character = 4;
            break;
        case 101:
            character = 5;
            break;
        case 102:
            character = 6;
            break;
        case 103:
            character = 7;
            break;
        case 104:
            character = 8;
            break;
        case 105:
            character = 9;
            break;
        case 107:
            character = '+';
            break;
        case 109:
            character = '-';
            break;
    }
    return character;
}

function updateField(insertPoint, valueBefore, html) {
    var parts = new Array();
    var opslag = insertPoint;
    insertPoint = updatedInsertPoint(insertPoint);//update insertPoint   
    parts[0] = (valueBefore.substring(0, insertPoint));
    parts[1] = (valueBefore.substring(insertPoint));
    currentFocus.innerHTML = parts[0] + html + parts[1];
    determineNewCaretPosition(opslag, (parts[1].length === 0));
}

function updatedInsertPoint(insertPoint) {
    //update insertPoint if valueBefore already contains sub or superscript



    var place = insertPoint;
    var i = 0;

    while (place > 0) {

        var opslag = currentFocus.childNodes[i];
        if (typeof (opslag) != 'undefined') {
            while (opslag.innerHTML != null) {//!== if in sub or sup tag
                opslag = opslag.innerHTML;

                insertPoint = insertPoint + 11;
            }
            place = place - opslag.length;
            i++;
        }
    }
    return insertPoint;
}
function setCaretPosition(pos) {

    var place = pos + 1;
    var go = true;
    var i = 0;
    while (go) {
        var textNode = currentFocus.childNodes[i];

        if (textNode != null) {
            while (textNode.innerHTML != null) {

                textNode = textNode.innerHTML;
            }
            if (place > textNode.length) {
                i++;

                place = place - textNode.length;
            }
            else {
                go = false;
            }

        }
        else {
            go = false;
        }

    }

    var range = document.createRange();
    range.setStart(currentFocus.childNodes[i], place);
    range.setEnd(currentFocus.childNodes[i], place);
    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);


}
function determineNewCaretPosition(insertPoint, isEnd) {//update caret position -> set behind inserted character

    setCaretPosition(insertPoint);

}

function sendInfo() {
    var content = new Array();
    for (var i = 0; i < enrichedElements.length; i++) {
        var str = enrichedElements[i].innerHTML;
        str = str.replace(/&nbsp;/gi, '');
        content.push(HtmlToDatabaseLanguage(str));
    }
    return content;
}

function HtmlToDatabaseLanguage(str) {
    str = str.replace(/\s+/, "");
    str = str.replace(new RegExp("→", 'g'), "#->#");
    str = str.replace(new RegExp("⇔", 'g'), "#<=>#");
    str = str.replace(new RegExp("</sub><sub>", 'g'), "");
    str = str.replace(new RegExp("</sup><sup>", 'g'), "");
    str = str.replace(new RegExp("</sub>", 'g'), "|");
    str = str.replace(new RegExp("</sup>", 'g'), "|");
    str = str.replace(new RegExp("<sub>", 'g'), "_|");
    str = str.replace(new RegExp("<sup>", 'g'), "^|");
    if (!(/\|(\+)?[0-9]*(\+)?/.test(str))) {
        str = str.replace("+", "#&#");
    }
    //str = str.replace(new RegExp("\\+", 'g'),"");
    str = str.replace(new RegExp("<br>", 'g'), "");
    str = str.replace(new RegExp("</br>", 'g'), "");
    str = str.replace(/\|\s*(.*)\s*\|/, "|$1|");// remove spaces within coefficients
    str = str.replace(/\|([0-9]+)-\|/g, "|-$1|");//OH2- ==> OH-2
    str = str.replace(/\|-\|/g, "|-1|");//OH- ==> OH-1
    str = str.replace(/\|+\|/g, "|1|");//OH+ ==> OH+1
    return str;
}


