<form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();">
    <p>Typ de gevraagde oplossing in het vakje. 
        Voor sub- of superscript klik je in het blauwe vakje, dat verschijnt als je op het veldje hebt geklikt, op X<sub>2</sub> voor sub- en X<sup>2</sup> voor superscript.
        Daarna klik je terug in het veldje en typ je het gewenste gatel in.
        Om terug normaal te typen klik je in het blauwe vakje op hetgeen je eerder had gekozen, om het uit te zetten.
        Vervolgens klik je op het veldje waardoor je terug overschakelt naar normale tekst.<br>
        
        Indien je antwoord een kommagetal is, kan je een komma of een punt gebruiken.
        Beiden worden geaccepteerd. Eenheden worden niet herkend.
        Er wordt een foutenmarge voorzien.
    </p>
    <legend id="qText"></legend>
    <p class="enriched form-control">Hier typen</p>
    <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
</form>