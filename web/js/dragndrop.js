//make hover css
makeHoverCSS();
//create draggable elements
createDraggableElements();
//create droppable elements
createDroppableElements();

function refreshDragNDrop() {
    createDraggableElements();
    createDroppableElements();
}

function makeHoverCSS() {
    $("<style type='text/css'> .dropZone{background-color:#D0CDC8;} .hoverDropZone{ background-color:#F8F5F0;} </style>").appendTo("head");
}

function createDraggableElements() {
    var div = $(".itemsToDrag");
    div.children().each(function () {
        $(this).addClass('well well-sm');
        $(this).css('display', 'inline-block');//make inline
        var yPosCursor = Math.floor($(this).height() / 2);
        var xPosCursor = Math.floor($(this).height() / 2);
        $(this).draggable({
            cursor: 'move',
            helper: 'clone',
            cursorAt: {left: xPosCursor,
                top: yPosCursor}
        });
        if (isNumeric($(this).html())) {
            $(this).css('background-color', '#D9F591');
        }
        if ($(this).html().trim() === '(leeg)') {
            $(this).css('background-color', '#D8A2F5');
        }
    });
}

function createDroppableElements() {
    var div = $("[class~=solutionZone]");
    div.find("*").each(function () {
        $(this).css('display', 'inline-block');//make div inline
    });
    div.find("[class~=dropZone]").each(function () {
        $(this).css('border-radius', '3px');
        $(this).css('padding', '9px');
        $(this).css('margin-left', '0.3em');
        $(this).droppable({
            drop: function (event, ui) {
                handleDrop(event, ui, $(this));
            },
            hoverClass: "hoverDropZone",
            tolerance: "pointer"
        });
    });
}

function handleDrop(event, ui, droppedOn) {
    droppedOn.html(ui.helper.html());
}

function isNumeric(num) {
    return !isNaN(num);
}

function sendDragAndDropInfo() {
    var content;
    var divSolution = $("[class~=solutionZone]");
    if (typeof (divSolution) !== 'undefined') {
        divSolution.find("p").each(function () {
            var innerHtml = $(this).html();
            if (typeof (innerHtml) !== 'undefined') {
                content += translatedContent(innerHtml.trim());
            }
        });
    }

    return removeEmptyFields(content);
}

function translatedContent(content) {
    if (content === '+') {
        return '#&#';
    }
    if (content.length === 1 && content !== "." && !isNumeric(content) && !(/\w/g.test(content))) {//arrow
        return '#->#';
    }
    content = content.replace(/<sub>/g, "_|");
    content = content.replace(/<sup>/g, "^|");
    content = content.replace(/<\/sub>/g, "|");
    content = content.replace(/<\/sup>/g, "|");
    return content;
}

function removeEmptyFields(content) {
    if (typeof content != 'undefined') {
        content = content.replace(/undefined/gi, "");
        content = content.replace(/\(leeg\)/gi, "");
        content = content.replace(/\.+/gi,"");//remove points
        content = content.replace(/(#&#)+/gi,"#&#");//remove double #&#
        content = content.replace(/#&#$/gi,"");//ends with #&#
        content = content.replace(/^#&#/gi,"");//begins with #&#
        content = content.replace(/(#&#)?(#->#)(#&#)?/,"#->#");
        return content;
    }
    return "";//undefined
}