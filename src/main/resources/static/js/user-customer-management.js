// src/main/resources/static/js/user-customer-management.js

document.addEventListener('DOMContentLoaded', function() {
    updateButtonVisibility();
});

function showCreateForm() {
    document.getElementById('createFormModal').style.display = 'block';
}

function showUpdateForm(cif) {
    fetch(`/user/customers/${cif}`)
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

function deleteCustomer(cif) {
    if (confirm('Are you sure you want to delete this customer?')) {
        fetch(`/user/customers/${cif}`, {
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

function hideCreateForm() {
    document.getElementById('createFormModal').style.display = 'none';
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

    fetch('/user/add-customer', {
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

    fetch(`/user/customers/${cif}`, {
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

function refreshCustomerTable() {
    fetch('/user/customers')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('tbody');
            tableBody.innerHTML = '';
            data.forEach((customer, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
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
                    <td>${customer.salary.toLocaleString()}</td>
                    <td>
                        <button onclick="showUpdateForm(${customer.cif})"
                                sec:authorize="hasAuthority('PERMISSION_UPDATE')">
                            Update
                        </button>
                        <button onclick="deleteCustomer(${customer.cif})"
                                sec:authorize="hasAuthority('PERMISSION_DELETE')">
                            Delete
                        </button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
            updateButtonVisibility();
        });
}