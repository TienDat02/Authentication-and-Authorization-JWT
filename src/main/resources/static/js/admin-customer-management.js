function showCreateForm() {
    document.getElementById('createFormModal').style.display = 'block';
}

function hideCreateForm() {
    document.getElementById('createFormModal').style.display = 'none';
}

function showUpdateForm(cif) {
    fetch(`/admin/customers/${cif}`)
        .then(response => response.json())
        .then(customer => {
            document.getElementById('updateCif').value = customer.cif;
            document.getElementById('updateEmpNo').value = customer.empNo;
            document.getElementById('updateName').value = customer.name;
            document.getElementById('updateEmail').value = customer.email;
            document.getElementById('updatePermAddress').value = customer.permAddress;
            document.getElementById('updateTempAddress').value = customer.tempAddress;
            document.getElementById('updatePhone').value = customer.phone;
            document.getElementById('updateBirthday').value = new Date(customer.birthday).toISOString().split('T')[0];
            document.getElementById('updateBirthPlace').value = customer.birthPlace;
            document.getElementById('updateGender').value = customer.gender;
            document.getElementById('updateSalary').value = customer.salary;
            document.getElementById('updateFormModal').style.display = 'block';
        });
}

function hideUpdateForm() {
    document.getElementById('updateFormModal').style.display = 'none';
}

document.getElementById('createCustomerForm').onsubmit = function(e) {
    e.preventDefault();

    const empNo = document.getElementById('empNo').value;
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const permAddress = document.getElementById('permAddress').value;
    const tempAddress = document.getElementById('tempAddress').value;
    const phone = document.getElementById('phone').value;
    const birthday = document.getElementById('birthday').value;
    const birthPlace = document.getElementById('birthPlace').value;
    const gender = document.getElementById('gender').value;
    const salary = document.getElementById('salary').value;

    fetch('/admin/add-customer', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            empNo: empNo,
            name: name,
            email: email,
            permAddress: permAddress,
            tempAddress: tempAddress,
            phone: phone,
            birthday: birthday,
            birthPlace: birthPlace,
            gender: gender,
            salary: salary
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to add customer');
        return response.json();
    })
    .then(customer => {
        alert('Customer added successfully!');
        hideCreateForm();
        refreshCustomerTable();
    })
    .catch(error => alert(error.message));
};

document.getElementById('updateCustomerForm').onsubmit = function(e) {
    e.preventDefault();

    const cif = document.getElementById('updateCif').value;
    const empNo = document.getElementById('updateEmpNo').value;
    const name = document.getElementById('updateName').value;
    const email = document.getElementById('updateEmail').value;
    const permAddress = document.getElementById('updatePermAddress').value;
    const tempAddress = document.getElementById('updateTempAddress').value;
    const phone = document.getElementById('updatePhone').value;
    const birthday = document.getElementById('updateBirthday').value;
    const birthPlace = document.getElementById('updateBirthPlace').value;
    const gender = document.getElementById('updateGender').value;
    const salary = document.getElementById('updateSalary').value;

    fetch(`/admin/customers/${cif}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            cif: cif,
            empNo: empNo,
            name: name,
            email: email,
            permAddress: permAddress,
            tempAddress: tempAddress,
            phone: phone,
            birthday: birthday,
            birthPlace: birthPlace,
            gender: gender,
            salary: salary
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to update customer');
        return response.json();
    })
    .then(customer => {
        alert('Customer updated successfully!');
        hideUpdateForm();
        refreshCustomerTable();
    })
    .catch(error => alert(error.message));
};

function deleteCustomer(cif) {
    if (confirm('Are you sure you want to delete this customer?')) {
        fetch(`/admin/customers/${cif}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) throw new Error('Failed to delete customer');
            alert('Customer deleted successfully!');
            refreshCustomerTable();
        })
        .catch(error => alert(error.message));
    }
}

function refreshCustomerTable() {
    fetch('/admin/customers')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('tbody');
            tableBody.innerHTML = '';
            data.forEach((customer, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><input type="checkbox" class="customer-checkbox" value="${customer.cif}"></td>
                    <td>${index + 1}</td>
                    <td>${customer.cif}</td>
                    <td>${customer.empNo}</td>
                    <td>${customer.name}</td>
                    <td>${customer.email}</td>
                    <td>${customer.permAddress}</td>
                    <td>${customer.tempAddress}</td>
                    <td>${customer.phone}</td>
                    <td>${new Date(customer.birthday).toISOString().split('T')[0]}</td>
                    <td>${customer.birthPlace}</td>
                    <td>${customer.gender}</td>
                    <td>${customer.salary}</td>
                    <td>
                        <button onclick="showUpdateForm(${customer.cif})">Update</button>
                        <button onclick="deleteCustomer(${customer.cif})">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        });
}

document.getElementById('selectAll').addEventListener('change', function() {
    const checkboxes = document.querySelectorAll('.customer-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = this.checked;
    });
});

function searchCustomers() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const tableRows = document.querySelectorAll('tbody tr');

    tableRows.forEach(row => {
        const idCell = row.querySelector('td:nth-child(3)');
        const nameCell = row.querySelector('td:nth-child(5)');
        if (idCell && nameCell) {
            const id = idCell.textContent.toLowerCase();
            const name = nameCell.textContent.toLowerCase();
            if (id.includes(searchTerm) || name.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        }
    });
}

document.getElementById('searchInput').addEventListener('input', searchCustomers);

function showMassSalaryUpdateForm() {
    const selectedCustomers = getSelectedCustomers();
    if (selectedCustomers.length === 0) {
        alert('Please select at least one customer.');
        return;
    }

    populateSelectedCustomersTable(selectedCustomers);
    document.getElementById('massSalaryUpdateModal').style.display = 'block';
    openTab(null, 'fixedUpdate');
}

function getSelectedCustomers() {
    const checkboxes = document.querySelectorAll('.customer-checkbox:checked');
    return Array.from(checkboxes).map(checkbox => {
        const row = checkbox.closest('tr');
        return {
            cif: checkbox.value,
            empNo: row.cells[3].textContent,
            name: row.cells[4].textContent,
            salary: row.cells[12].textContent
        };
    });
}

function populateSelectedCustomersTable(customers) {
    const tableBody = document.querySelector('#selectedCustomersTable tbody');
    tableBody.innerHTML = '';
    customers.forEach((customer, index) => {
        const row = tableBody.insertRow();
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${customer.cif}</td>
            <td>${customer.empNo}</td>
            <td>${customer.name}</td>
            <td>${customer.salary}</td>
        `;
    });
}

function hideMassSalaryUpdateForm() {
    document.getElementById('massSalaryUpdateModal').style.display = 'none';
}

function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    if (evt) evt.currentTarget.className += " active";
}

document.getElementById('fixedUpdateForm').onsubmit = function(e) {
    e.preventDefault();
    const newSalary = document.getElementById('fixedSalary').value;
    updateSalaries('fixed', newSalary);
};

document.getElementById('raiseDeductionForm').onsubmit = function(e) {
    e.preventDefault();
    const amount = document.getElementById('amount').value;
    const operation = document.getElementById('operation').value;
    updateSalaries(operation, amount);
};

function updateSalaries(type, amount) {
    const selectedCustomers = getSelectedCustomers();

    if (selectedCustomers.length === 0) {
        alert('Please select at least one customer.');
        return;
    }

    fetch('/admin/mass-salary-update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            customerIds: selectedCustomers.map(c => c.cif),
            updateType: type,
            amount: parseFloat(amount)
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to update salaries');
        return response.json();
    })
    .then(result => {
        alert('Salaries updated successfully!');
        hideMassSalaryUpdateForm();
        refreshCustomerTable();
    })
    .catch(error => alert(error.message));
}