
<form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- partial--> 
    <legend id="qText"></legend>
    <div class="itemsToDrag">
        <!--items-->
    </div>   
    <br>
    <strong>Oplossing:</strong>
    <br>
    <div class="solutionZone">
        <div id="before"></div>
        <p id="arrowSymbol">&rarr;</p>
        <div id="after"></div> 
    </div>
    <br>
    <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
</form>
