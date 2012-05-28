<html>
<head>
</head>
<body>


<table>

<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);
  require '../../sdk/ElasticSearch.php';

  $es = ElasticSearch::getES();
  $a=$es->fruit();
  
  var_dump($a);
  $response = $es->query('course','title:painting');

  $hitcount = $response->hits->total;
  $duration = $response->took;
  $hits = $response->hits->hits;

  foreach ( $hits as $hit ) {
    $coursetitle = $hit->_source->title;
    $provider = $hit->_source->provtitle;

print <<<END
<tr><td>$coursetitle</td><td>$provider</td></tr>
END;

  }
?>

</table>

</body>
</html>
