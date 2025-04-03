document.addEventListener("DOMContentLoaded", function() {
    const personNodes = document.querySelectorAll('.person-node');

    personNodes.forEach(node => {
        node.addEventListener('click', function() {
            const personId = this.dataset.id;
            fetchPersonDetails(personId);
        });
    });

    function fetchPersonDetails(personId) {
        fetch(`/person/${personId}`)
            .then(response => response.json())
            .then(data => {
                displayPersonDetails(data);
            })
            .catch(error => console.error('Error fetching person details:', error));
    }

    function displayPersonDetails(person) {
        const detailsContainer = document.getElementById('person-details');
        detailsContainer.innerHTML = `
            <h2>${person.firstName} ${person.middleName ? person.middleName + ' ' : ''}${person.lastName}</h2>
            <img src="${person.portraitUrl}" alt="${person.firstName} ${person.lastName}" />
            <p><strong>Born:</strong> ${person.birthDate ? person.birthDate : 'N/A'}</p>
            <p><strong>Died:</strong> ${person.deathDate ? person.deathDate : 'N/A'}</p>
            <p><strong>Gender:</strong> ${person.gender}</p>
            <p><strong>Description:</strong> ${person.description ? person.description : 'No description available.'}</p>
        `;
        detailsContainer.style.display = 'block';
    }

    function showPersonDetails(personId) {
        // Fetch person details from the backend (replace with actual API call)
        fetch(`/person/${personId}`)
            .then(response => response.json())
            .then(person => {
                document.getElementById("person-name").textContent = `${person.firstName} ${person.lastName}`;
                document.getElementById("person-years").textContent = `${person.birthDate || "Unknown"} - ${person.deathDate || "Present"}`;
                document.getElementById("person-description").textContent = person.description || "No description available.";
                document.getElementById("person-details").style.display = "block";
            });
    }

    function closeModal() {
        document.getElementById("person-details").style.display = "none";
    }
});