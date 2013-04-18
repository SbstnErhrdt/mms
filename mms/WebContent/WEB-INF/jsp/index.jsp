<!doctype html>
<html>
        <head>
                <title>SOPRA- MMS Teildurchstich</title>
                <link rel="stylesheet" type="text/css" href="static/css/style.css">
                <link rel="stylesheet" type="text/css" href="static/css/reset.css">
        </head>
        <body>
                <div id ="formular">
                        <h1>SOPRA MMS Teildurchstich - Please Log in</h1>
                        <form action="./login" method="POST">
                                <ul>
                                        <li>Email-Adresse: </li>
                                        <li><input name="email" type="text"value="Michaela@gmx.net"></li>
                                </ul>
                                <ul>
                                        <li>Passwort: </li>
                                        <li><input name="password" type="password"value=""></li>
                                </ul>
                                <ul>
                                        <li><input type="submit" value=" Absenden "></li>
                                        <li><a href="./register">Registrieren</a></li>
                                </ul>
                        </form>
                </div>
        </body>
</html>