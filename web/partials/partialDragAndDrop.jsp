
<form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- partial--> 
    <p>Sleep met de muis de juiste elementen in de velden.<br>
        Indien je fout was en het veld wil leeg maken kan je het paarse blokje met tekst '(leeg)' gebruiken om het veld leeg te maken.<br>
        <strong>Opgelet!</strong> Het is perfect mogelijk dat niet alle velden ingevuld zijn!
    </p>
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
