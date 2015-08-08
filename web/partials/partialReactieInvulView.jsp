<div class="radio well bs-component">
    <form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven--> 
        
        <legend id="qText">${qText}</legend>


        <p id="arrowSymbol" style="text-align: center;float:left; padding-top: 1em;">&rarr;</p>

        <br><br><br>
        <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
    </form> 
</div>