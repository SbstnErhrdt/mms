
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Modul Management System - Universit&auml;t Ulm</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Fav and touch icons -->
    <link rel="shortcut icon" href="../assets/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">


  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="index.php">Modul Management System</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li><a href="index.php">Home</a></li>
              <li><a href="index.php?page=handbucher">Modulhanbuch</a></li>
              <li><a href="index.php?page=module">Module</a></li>
              <li><a href="index.php?page=veranstaltungen">Veranstaltungen</a></li>
            </ul>
              <ul class="nav pull-right">
                <li style="margin-top: 5px;" class="input-append">
                  <input class="span2" id="appendedInputButton" type="text">
                  <button class="btn" type="button">Suche</button>
                </li>
              <li><a href="index.php?page=user">Benutzerverwaltung</a></li>
              <li><a href="index.php">Logout</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <?php
    if (isset($_GET['page'])) {
		$page = $_GET['page'].'.php';
		if (file_exists($page)) {
			include($page);
		} else {
			include('404.php');
		}
	} else {
		include('home.php');
	}
	?>

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/robin.js"></script>


    <footer>

    <div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
		<hr/>
		<strong>Modul Manangement System</strong> - Universit&auml;t Ulm<br /><small>&copy; 2012 Sebastian Erhardt, Robin Z&ouml;ller, Eugen Nabiev</small>
		<hr/>
		</div>
		<br />
	</div>
</div>




    </footer>

  </body>
</html>