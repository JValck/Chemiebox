<div class="radio well bs-component">
    <p>Vervolledig onderstaande reactievergelijking. Er zijn voldoende vakjes voorzien.                        
    </p>
    <p>Typ de gevraagde oplossing in het vakje. 
        Voor sub- of superscript klik je in het blauwe vakje, dat verschijnt als je op het veldje hebt geklikt, op X<sub>2</sub> voor sub- en X<sup>2</sup> voor superscript.
        Daarna klik je terug in het veldje en typ je het gewenste gatel in.
        Om terug normaal te typen klik je in het blauwe vakje op hetgeen je eerder had gekozen, om het uit te zetten.
        Vervolgens klik je op het veldje waardoor je terug overschakelt naar normale tekst.<br>
        
        Indien je antwoord een kommagetal is, kan je een komma of een punt gebruiken.
        Beiden worden geaccepteerd. Eenheden worden niet herkend.
        Er wordt een foutenmarge voorzien.
    </p>
    <form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven--> 
        
        <legend id="qText">${qText}</legend>


        <p id="arrowSymbol" style="text-align: center;float:left; padding-top: 1em;">&rarr;</p>

        <br><br><br>
        <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
    </form> 
</div>