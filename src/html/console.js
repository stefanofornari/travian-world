MAP_SQUARE = 7;
MAP_SIZE = 5*MAP_SQUARE;
MAP_COLUMNS = 3*MAP_SIZE;
MAP_ROWS = 3*MAP_SIZE;
FIRST_ROW = 0;
FIRST_COLUMN = 0;

WAIT = 2000;

var map;

function hashCode(s) {
  var i, hash;
  
  hash = 0;
  for (i=0; i<s.length; ++i) {
    hash = 31*hash + s.charCodeAt(i)
  }
  
  return hash;
}

function randomWait() {
  return 2000 + Math.random()*1500;
}

function debug(msg) {
  document.getElementById("debug").innerHTML += ("<br/>" + msg);
}

function updateResources() {
  var lumber, clay, iron, crop;
  
  lumber = parent.travian.document.getElementById("l4");
  clay = parent.travian.document.getElementById("l3");
  iron = parent.travian.document.getElementById("l2");
  crop = parent.travian.document.getElementById("l1");
  document.getElementById("body").innerHTML 
    = "<img src='http://s4.travian.com/img/un/r/1.gif' alt='Lumber'> " + lumber.innerText + "<br/>"
    + "<img src='http://s4.travian.com/img/un/r/2.gif' alt='Clay'>  " + clay.innerText + "<br/>"
    + "<img src='http://s4.travian.com/img/un/r/3.gif' alt='Iron'> " + iron.innerText + "<br/>"
    + "<img src='http://s4.travian.com/img/un/r/4.gif' alt='Crop'> " + crop.innerText + "<br/>";
}

/**
 * position the map so that the given coordinates are in the upper-left corner
 */
function positionMap(x, y) {
  var form;
  
  form = parent.travian.document.forms[0];
  form.elements["xp"].value = x+3;
  form.elements["yp"].value = y+3;
  form.submit();
  
  window.setTimeout("generatePartialMap(" + x + "," + y + ")", randomWait());
}

function generatePartialMap(x, y) {
  var data, i, j, k, l, mapcelly, mapcellx, innercelly, innercellx;
  
  data = parent.travian.m_c.ad;
 
  for(i=0; i<MAP_SQUARE; ++i) {
    for (j=0; j<MAP_SQUARE; ++j) {
      mapcelly = Math.floor((y+i)/MAP_SIZE);
      mapcellx = Math.floor((x+j)/MAP_SIZE);
      innercelly = (y+i)%MAP_SIZE;
      innercellx = (x+j)%MAP_SIZE;
      
      if (null == parent.travian.m_c.ad[j][i].name) {
        map[mapcelly][mapcellx][innercelly][innercellx].player  = "";
      	map[mapcelly][mapcellx][innercelly][innercellx].village = "";
      	map[mapcelly][mapcellx][innercelly][innercellx].ally    = "";  	
      } else {
        map[mapcelly][mapcellx][innercelly][innercellx].player  = data[j][i].name;
      	map[mapcelly][mapcellx][innercelly][innercellx].village = data[j][i].dname;
      	map[mapcelly][mapcellx][innercelly][innercellx].ally    = data[j][i].ally;
      }
    }
  }
  
  if (x < (MAP_COLUMNS-MAP_SQUARE-1)) {
    positionMap(x+MAP_SQUARE, y);
  } else {
    if (y < (MAP_ROWS-MAP_SQUARE-1)) {
      positionMap(0, y+MAP_SQUARE);
    } else {
      endMap();
    }
  }
}

function endMap() {
  var html, i, j, k, l;
  
  html = "<div class='map'>";
  
  for (i=0; i<map.length; ++i) {
    html += "<div class='maprow'>";
    for(j=0; j<map[i].length; ++j) {
      html += "<table class='mapcell'>";
    
      for (k=0; k<map[i][j].length; ++k) {
        html += "<tr>";
        for (l=0; l<map[i][j][k].length; ++l) {
          html = html
             + "<td class='a" + hashCode(map[i][j][k][l].ally) + "'><div class='innercell'>"
             + "(" + (i*MAP_SIZE+k) + "," + (j*MAP_SIZE+l) + ")"
             + "<br/>"
             + map[i][j][k][l].village
             + "<br/>"
             + map[i][j][k][l].player
             + "<br/>"
             + map[i][j][k][l].ally
             + "</div></td>"
             ;
        }
        html += "</tr>";
      }
      html += "</table>";
    }
    html += "</div><br/>";
  }
  html += "</div>";   
  
  document.getElementById("body").innerHTML = html;
}

function updateResources() {
  var lumber, clay, iron, crop;
  
  lumber = parent.travian.document.getElementById("l4");
  clay = parent.travian.document.getElementById("l3");
  iron = parent.travian.document.getElementById("l2");
  crop = parent.travian.document.getElementById("l1");
  document.getElementById("body").innerHTML 
    = "<img src='http://s4.travian.com/img/un/r/1.gif' alt='Lumber'> " + lumber.innerText + "<br/>"
    + "<img src='http://s4.travian.com/img/un/r/2.gif' alt='Clay'>  " + clay.innerText + "<br/>"
    + "<img src='http://s4.travian.com/img/un/r/3.gif' alt='Iron'> " + iron.innerText + "<br/>"
    + "<img src='http://s4.travian.com/img/un/r/4.gif' alt='Crop'> " + crop.innerText + "<br/>";
}

function generateMap() {
  var i, j, k, l;
  
  parent.travian.document.location.href="http://s4.travian.com/karte.php";
  
  map = new Array(Math.ceil(MAP_ROWS/MAP_SIZE));
  for (i=0; i<map.length; ++i) {
    map[i] = new Array(Math.ceil(MAP_ROWS/MAP_SIZE));
    for (j=0; j<map[i].length; ++j) {
      map[i][j] = new Array(MAP_SIZE);
      for (k=0; k<map[i][j].length; ++k) {
        map[i][j][k] = new Array(MAP_SIZE);
        for (l=0; l<map[i][j][k].length; ++l) {
          map[i][j][k][l] = new Object();
        }
      }
    }
  }
  
  window.setTimeout("positionMap(" + FIRST_ROW + "," + FIRST_COLUMN + ",0)", 1000);
}

function generateReport() {
}