$(document).ready(function () {
    $('.ems_sorttbl').each(function (k) {
        $('#ems_tbsort' + k + ' th').each(function (i) {
			if ($(this).hasClass('ems_thsort' + k)) {
			    if ($(this).find('.th-content').length === 0) {
			        const titleText = $(this).text().trim();
			        $(this).empty();
			        const wrapper = $('<span>', {class: 'th-content'});
			        const icon = $('<i>', {class: 'fa fa-sort-amount-asc fa-lg sort-icon','aria-hidden': 'true'}).on('click', function () {sortTable(i, this);});

			        wrapper.append(icon).append(titleText);
			        $(this).append(wrapper);
			    }
				const styles = `th {
				                    white-space: nowrap;
				                }
				                .th-content {
				                    display: inline-flex;
				                    align-items: center;
				                    gap: 6px;
				                    white-space: nowrap;
				                }
				                .sort-icon {
				                    color: gray;
				                    cursor: pointer;
				                    flex-shrink: 0;
				                }`;

				            $('<style>')
				                .prop('type', 'text/css')
				                .html(styles)
				                .appendTo('head');
			}
        });

       /* let elementColumnIndex = 1;
        let elementIconEl = document.querySelector(
            '#ems_tbsort' + k + ' th.ems_thsort' + k + ' .sort-icon'
        );

        if (elementIconEl) {
            sortTable(elementColumnIndex, elementIconEl);
        }
		*/
    });
});

$(document).ready(function () {
    $('.ems_sorttbl').each(function (k) {
        $('#ems_tbsort' + k + ' th').each(function (i) {
			if ($(this).hasClass('ems_innerthsort' + k)) {
			    if ($(this).find('.th-content').length === 0) {
			        const titleText = $(this).text().trim();
			        $(this).empty();
			        const wrapper = $('<span>', {class: 'th-content'});
			        const icon = $('<i>', {class: 'fa fa-sort-amount-asc fa-lg sort-icon','aria-hidden': 'true'}).on('click', function () {sortGroups(i, i,this);});

			        wrapper.append(icon).append(titleText);
			        $(this).append(wrapper);
			    }
				const styles = `th {
				                    white-space: nowrap;
				                }
				                .th-content {
				                    display: inline-flex;
				                    align-items: center;
				                    gap: 6px;
				                    white-space: nowrap;
				                }
				                .sort-icon {
				                    color: gray;
				                    cursor: pointer;
				                    flex-shrink: 0;
				                }`;

				            $('<style>')
				                .prop('type', 'text/css')
				                .html(styles)
				                .appendTo('head');
			}
        });

       /* let elementColumnIndex = 1;
        let elementIconEl = document.querySelector(
            '#ems_tbsort' + k + ' th.ems_thsort' + k + ' .sort-icon'
        );

        if (elementIconEl) {
            sortTable(elementColumnIndex, elementIconEl);
        }
		*/
    });
});

let sortState = {};          
let originalRows = {};      

function sortTable(colIndex, iconEl) {

	let table = iconEl.closest("table");
    let tbody = table.tBodies[0];
    let rows = Array.from(tbody.rows);
	
    if (
        rows.length === 0 ||
        (rows.length === 1 && rows[0].cells.length === 1)
    ) {
        return;
    }

    let tableId = table.id;
    let key = tableId + "_" + colIndex;

    sortState[key] = (sortState[key] || 0) + 1;
    if (sortState[key] > 2) sortState[key] = 0;

    table.querySelectorAll("thead i.fa").forEach(i => {
        i.classList.remove("active", "fa-sort-amount-desc");
        i.classList.add("fa-sort-amount-asc");
        i.style.color = "#999";
    });

    let asc = sortState[key] === 1;

    if (sortState[key] !== 0) { 
        iconEl.classList.remove("fa-sort-amount-asc", "fa-sort-amount-desc");
        iconEl.classList.add(asc ? "fa-sort-amount-asc" : "fa-sort-amount-desc");
        iconEl.classList.add("active");
        iconEl.style.color = "red";
    }

    if (!originalRows[tableId]) {
        originalRows[tableId] = rows.slice();
    }

    if (sortState[key] === 0) {
        originalRows[tableId].forEach(row => tbody.appendChild(row));
        return;
    }

	rows.sort((a, b) => {
	    let x = a.cells[colIndex].innerText.trim();
	    let y = b.cells[colIndex].innerText.trim();

	    let rx = parseDateRange(x);
	    let ry = parseDateRange(y);

	    if (rx && ry) {
	        let diff = rx.start - ry.start;
	        if (diff === 0) diff = rx.end - ry.end;
	        return asc ? diff : -diff;
	    }

	    let dx = parseDate(x);
	    let dy = parseDate(y);

	    if (dx && dy) {
	        return asc ? dx - dy : dy - dx;
	    }

	    return asc ? naturalCompare(x, y) : naturalCompare(y, x);
	});


    rows.forEach(row => tbody.appendChild(row));
}

function naturalCompare(a, b) {

    let ax = a.toLowerCase().match(/(\d+|\D+)/g);
    let bx = b.toLowerCase().match(/(\d+|\D+)/g);

    if (!ax || !bx) return a.localeCompare(b);

    let len = Math.min(ax.length, bx.length);

    for (let i = 0; i < len; i++) {

        let an = parseInt(ax[i], 10);
        let bn = parseInt(bx[i], 10);

        if (!isNaN(an) && !isNaN(bn)) {
            if (an !== bn) return an - bn;
        } else {
            if (ax[i] !== bx[i]) {
                return ax[i].localeCompare(bx[i]);
            }
        }
    }
    return ax.length - bx.length;
}

function parseDate(str) {
    if (!str) return null;

    str = str.trim();

    if (/^\d{4}-\d{2}-\d{2}$/.test(str)) {
        return new Date(str);
    }

    let m = str.match(/^(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})$/);
    if (m) {
        return new Date(m[3], m[2] - 1, m[1]);
    }

    let d = new Date(str);
    return isNaN(d) ? null : d;
}

function parseDateRange(str) {
    if (!str) return null;

    let parts = str.split(/\s*-\s*/);
    if (parts.length !== 2) return null;

    let start = parseDate(parts[0]);
    let end = parseDate(parts[1]);

    if (!start || !end) return null;

    return { start, end };
}

function globalSearchTable(inputEl, tableIndex) {
    let filter = inputEl.value.toLowerCase();
 
    let tableId = 'ems_table' + tableIndex;
    let table = document.getElementById(tableId); 
 
	let tr = table.querySelectorAll("tbody tr");
 
    for (let i = 0; i < tr.length; i++) {
        let tds = tr[i].getElementsByTagName("td"); 
        let rowMatch = false;
 
        for (let j = 0; j < tds.length && !rowMatch; j++) {
            let td = tds[j];
            let text = td.textContent.toLowerCase(); 
 
            if (text.includes(filter)) {
                rowMatch = true; 
                break;
            }
            let inputs = td.querySelectorAll("input, textarea");
            inputs.forEach(el => {
                if (el.value && el.value.toLowerCase().includes(filter)) {
                    rowMatch = true; 
                }
            });
            if (rowMatch) break;
        }
        tr[i].style.display = rowMatch ? "" : "none";
    }
}

function globalInnerSearchTable(inputEl, tableIndex) {

    let filter = inputEl.value.toLowerCase().trim();
    let table = document.getElementById('ems_table' + tableIndex);

    let parents = table.querySelectorAll("[class*='parent-row']");

    parents.forEach((parent) => {

        let parentClass = [...parent.classList].find(cls => cls.startsWith("parent-row"));
        let index = parentClass.replace("parent-row", "");

        let headingRow = table.querySelector(".child-heading" + index);
        let childRows = table.querySelectorAll(".child-row" + index);

        let tbody = headingRow ? headingRow.closest("tbody") : null;

        let parentText = parent.innerText.toLowerCase();
        let parentMatch = filter && parentText.includes(filter);

        let childMatchFound = false;

        childRows.forEach(child => {
            if (child.innerText.toLowerCase().includes(filter)) {
                childMatchFound = true;
            }
        });

        if (parentMatch) {
            parent.style.display = "";

            if (tbody) tbody.style.display = "table-row-group";
            if (headingRow) headingRow.style.display = "";

            childRows.forEach(row => row.style.display = "");
        }

        else if (!parentMatch && childMatchFound) {
			
            parent.style.display = "";

            if (tbody) tbody.style.display = "table-row-group";
            if (headingRow) headingRow.style.display = "";

            childRows.forEach(row => {
                row.style.display = row.innerText.toLowerCase().includes(filter) ? "" : "none";
            });

        }

        else if (!filter) {
            parent.style.display = "";

            if (tbody) tbody.style.display = "none";

            childRows.forEach(row => row.style.display = "");
            if (headingRow) headingRow.style.display = "";
        }

        else {
            parent.style.display = "none";
            if (tbody) tbody.style.display = "none";
        }

    });
}

//BP sorting for inner table
let sortState_0 = {};          
let originalOrder = [];   
let groups = [];   
//let table;  

function sortGroups(colIndex, key, iconEl) {
	
	const table = iconEl.closest("table");
	let allParentRows = Array.from(table.querySelectorAll("tr[class^='parent-row']"));
		
	 groups = allParentRows.map((parentRow, index) => {
	    let parentTbody = parentRow.parentElement;
	    let next = parentTbody.nextElementSibling;
	    let childTbody = (next && next.tagName === "TBODY" && next.id.startsWith("tbody")) ? next : null;
	
	    return {
	        parentRow,
	        parentTbody,
	        childTbody,
	        index
	    };
	});

	if (originalOrder.length === 0) {
		 originalOrder = groups.map(g => ({
		    parentTbody: g.parentTbody,
		    childTbody: g.childTbody
		}));
	}
	
   sortState_0[key] = (sortState_0[key] || 0) + 1;
    if (sortState_0[key] > 2) sortState_0[key] = 0;
    
    if (sortState_0[key] === 0) {
        resetOrder(table);
        return;
    }

    let asc = sortState_0[key] === 1;
    
    groups.sort((a, b) => {
		let x = (a.parentRow.cells[colIndex] ? a.parentRow.cells[colIndex].innerText : "").trim();
		let y = (b.parentRow.cells[colIndex] ? b.parentRow.cells[colIndex].innerText : "").trim();

        let dx = Date.parse(x);
        let dy = Date.parse(y);
        if (!isNaN(dx) && !isNaN(dy)) return asc ? dx - dy : dy - dx;

        return asc ? x.localeCompare(y) : y.localeCompare(x);
    });

    groups.forEach(g => {
        if (g.parentTbody.parentNode) g.parentTbody.remove();
        if (g.childTbody && g.childTbody.parentNode) g.childTbody.remove();

        table.appendChild(g.parentTbody);
        if (g.childTbody) table.appendChild(g.childTbody);
    });
    
    table.querySelectorAll("thead i.fa").forEach(i => {
        i.classList.remove("active", "fa-sort-amount-desc");
        i.classList.add("fa-sort-amount-asc");
        i.style.color = "#999";
    });
    
    if (sortState_0[key] !== 0) {
        iconEl.classList.remove("fa-sort-amount-asc", "fa-sort-amount-desc");
        iconEl.classList.add(asc ? "fa-sort-amount-asc" : "fa-sort-amount-desc");
        iconEl.classList.add("active");
        iconEl.style.color = "red";
    }
}

function toggleChild(groupIndex) {
	const childTbody = document.getElementById("tbody" + groupIndex);
	const icon = document.getElementById("hidCont" + groupIndex);
    if (!childTbody) return;

    if (childTbody.style.display === "none") {
        childTbody.style.display = "";
        if (icon) icon.classList.replace("fa-expand", "fa-compress");
    } else {
        childTbody.style.display = "none";
        if (icon) icon.classList.replace("fa-compress", "fa-expand");
    }
}
	
function resetOrder(table) {
    groups.forEach(g => {
        if (g.parentTbody.parentNode) g.parentTbody.remove();
        if (g.childTbody && g.childTbody.parentNode) g.childTbody.remove();
    });
    originalOrder.forEach(g => {
        table.appendChild(g.parentTbody);
        if (g.childTbody) table.appendChild(g.childTbody);
    });

	Object.keys(sortState_0).forEach(function(k) {
	    sortState_0[k] = 0;
	});

    table.querySelectorAll("thead i.fa").forEach(i => {
        i.classList.remove("active", "fa-sort-amount-desc", "fa-sort-amount-asc");
        i.classList.add("fa-sort-amount-asc");
        i.style.color = "#999";
    });
}