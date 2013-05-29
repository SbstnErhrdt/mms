<div class="container-fluid">
	<div class="row-fluid">
		<ul class="span 12 breadcrumb">
		<li><a href="index.php">Home</a> <span class="divider">/</span></li>
		<li class="active">Module freigeben</li>
		</ul>
	</div>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span9">
		<h1>Freigeben</h1>
		<p class="lead"> Hier k&ouml;nen alle Benutzer bearbeitet werden.</p>

		<a href="index.php?page=create&name=Benutzer&action=Erstellen" class="btn btn-success"><i class="icon-edit icon-white"></i> Neuen Benutzer erstellen</a>


		<hr/>

		<table class="table">
	    	<thead>
				<tr>
					<td>#</td>
					<td>Name</td>
					<td>Rechte</td>
					<td>Rolle</td>
					<td>Stellvertreter</td>
					<td>L&ouml;schen</td>
				</tr>
	    	</thead>
	    	<tbody>
				<tr>
					<td>1</td>
					<td><strong>Alexander Nassal</strong></td>
					<td>Alles bearbeiten<br/>
						<button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td>Admin<br/><button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td>Prof. Slomka<br/><button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td><button class="btn btn-mini btn-danger" type="button">L&ouml;schen</button></td>
				</tr>
				<tr>
					<td>2</td>
					<td><strong>Prof. Dr. Dr. Dadam</strong></td>
					<td>Modul Praktische Informatik bearbeiten<br/>
						<button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td>Modulverantwortlicher<br/>
						<button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td>Prof. Slomka<br/><button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td><button class="btn btn-mini btn-danger" type="button">L&ouml;schen</button></td>
				</tr>
				<tr>
					<td>3</td>
					<td><strong>Prof. Dr. Dr. Uwe Sch&ouml;ning</strong></td>
					<td>Modul Theoretische Informatik bearbeiten<br/>
						<button class="btn btn-mini btn-warning" type="button">bearbeiten</button>
					</td>
					<td>Modulverantwortlicher<br/><button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td>Prof. Slomka<br/><button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td><button class="btn btn-mini btn-danger" type="button">L&ouml;schen</button></td>
				</tr>
				<tr>
					<td>4</td>
					<td><strong>Prof. Slomka</strong></td>
					<td>Modul Informatik bearbeiten<br/>
					<button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td><strong>Peli</strong>de<strong>kan</strong><br/>
						<button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
						<td>Alexander Nassal<br/><button class="btn btn-mini btn-warning" type="button">bearbeiten</button></td>
					<td><button class="btn btn-mini btn-danger" type="button">L&ouml;schen</button></td>
				</tr>
	    	</tbody>
		</table>
		</div>
		<div class="span3">

		</div>
	</div>
</div>