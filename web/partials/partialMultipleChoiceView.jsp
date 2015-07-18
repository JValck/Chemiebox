
        <form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven--> 
            <legend id="qText"></legend>
            <div style="padding-left: 3em;" class="radio well bs-component">
                <div class="options"></div>
            </div><br>
            <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
        </form>
        