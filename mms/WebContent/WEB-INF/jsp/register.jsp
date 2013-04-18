<!doctype html>
<html>
	<head>
		<title>SOPRA- MMS Teildurchstich</title>
		<link rel="stylesheet" type="text/css" href="static/css/style.css">
		<link rel="stylesheet" type="text/css" href="static/css/reset.css">
	</head>
	<body>
		<div id ="formular">
			<h1>Registrieren</h1>
			<form action="./completeregister" method="POST">
				<ul>
					<li>Vorname: </li>
					<li><input name="firstName" type="text" value=""></li>
				</ul>
				<ul>
					<li>Nachname: </li>
					<li><input name="lastName" type="text" value=""></li>
				</ul>
				<ul>
					<li>Titel: </li>
					<li><input name="title" type="text"value=""></li>
				</ul>
				<ul>
					<li>E-Mail: </li>
					<li><input name="email" type="text"value=""></li>
				</ul>
				<ul>
					<li>Passwort: </li>
					<li><input name="password" type="text"value=""></li>
				</ul>
				<ul>
					<li>Abschluss: </li>
					<li><input name="graduation" type="text"value=""></li>
				</ul>
				<ul>
					<li>Matrikelnummer: </li>
					<li><input name="matricNum" type="text"value=""></li>
				</ul>
				<ul>
					<li>Semester: </li>
					<li><input name="semester" type="text"value=""></li>
				</ul>
				<ul>
					<li><input type="submit" value=" Absenden "></li>
					<li></li>
				</ul>
			</form>
		</div>
	</body>
</html>