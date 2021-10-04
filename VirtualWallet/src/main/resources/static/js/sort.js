
function sortTableAmount() {
    let table, rows, switching, i, x, y, dir, shouldSwitch, switchcount = 0;
    table = document.getElementById("dataTable");
    switching = true;
    dir = "asc";
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[0];
            y = rows[i + 1].getElementsByTagName("TD")[0];
            if (dir === "asc") {
                if (parseFloat(x.innerHTML) > parseFloat(y.innerHTML)) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir === "desc") {
                if (parseFloat(x.innerHTML) < parseFloat(y.innerHTML)) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount === 0 && dir === "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }

}

function sortTableDate() {
    let table, rows, switching, i, x, y, dir, shouldSwitch, switchcount = 0;
    table = document.getElementById("dataTable");
    switching = true;
    dir = "asc";
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[3];
            let momentDateX = moment(x.innerHTML, 'YYYY-MM-DD HH:mm:ss')
            y = rows[i + 1].getElementsByTagName("TD")[3];
            let momentDateY = moment(y.innerHTML, 'YYYY-MM-DD HH:mm:ss')
            if (dir === "asc") {
                if (moment(momentDateX).isAfter(momentDateY)) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir === "desc") {
                if (moment(momentDateX).isBefore(momentDateY)) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount === 0 && dir === "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }

}