document.addEventListener("DOMContentLoaded", function() {
    const addPersonBtn = document.getElementById("add-person-btn");
    const addPersonModal = document.getElementById("add-person-modal");

    addPersonBtn.addEventListener("click", function () {
        addPersonModal.style.display = "block";
    });

    window.closeAddPersonModal = function () {
        addPersonModal.style.display = "none";
    };
});
function getSpouse(personId) {
    const relationsUrl = `${apiPath}/relations`;

    return fetch(relationsUrl)
        .then(response => {
            if (!response.ok) {
                console.error('ERROR: return code is', response.status);
                throw new Error('Failed to fetch relations');
            }
            return response.json();
        })
        .then(data => {
            const spouseRelation = data._embedded.relations.find(relation => 
                (relation.person1 === `/api/persons/${personId}` || relation.person2 === `/api/persons/${personId}`) &&
                relation.relationType === 'SPOUSE'
            );

            if (spouseRelation) {
                return spouseRelation.person1 === `/api/persons/${personId}` 
                    ? spouseRelation.person2.replace('/api/persons/', '') 
                    : spouseRelation.person1.replace('/api/persons/', '');
            } else {
                console.log('No spouse found for the given person.');
                return null;
            }
        })
        .catch(error => {
            console.error('ERROR:', error);
            throw error;
        });
}
const apiPath = '/api';

function addRelation(person1, person2, relationType) {
    const relationData = {
        person1: person1,
        person2: person2,
        type: relationType
    };

    fetch(apiPath + '/relations', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(relationData)
    })
        .then(response => {
            if (!response.ok) {
                console.error('ERROR: return code is', response.status);
                throw new Error('Failed to create relation');
            }
            return response.json();
        })
        .then(data => {
            alert('Relation added successfully!');
            console.log('Relation added!');
        })
        .catch(error => {
            console.error('ERROR:', error);
            alert('ERROR adding relation. Please try again.');
        });
}

function addPerson() {
    const form = document.getElementById('add-person-form');
    const formData = new FormData(form);

    const personData = {
        firstName: formData.get('firstName'),
        middleName: formData.get('middleName'),
        lastName: formData.get('lastName'),
        birthDate: formData.get('birthDate'),
        deathDate: formData.get('deathDate'),
        gender: formData.get('gender'),
        description: formData.get('description'),
        portraitUrl: formData.get('portraitUrl')
    };

    //alert('!!! Adding person:' + JSON.stringify(personData));
    console.log('Hi');

    fetch(apiPath + '/persons', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(personData)
    })
        .then(response => {
            if (!response.ok) {
                console.error('ERROR: return code is', response.status);
                throw new Error('Failed to create person');
            }
            return response.json();
        })
        .then(data => {
            alert('Person added successfully!');
            console.log('Person added!');

            const selfRef = data._links.self.href
            const relationType = formData.get('relationType');
            const relativeId = formData.get('relative');
            const relativeRef = 'api/persons/' + relativeId;
            if (relationType === 'CHILD') {
                addRelation(relativeRef, selfRef, relationType);
            } else if (relationType === 'PARENT') {
                addRelation(selfRef, relativeRef, 'CHILD');
            } else if (relationType === 'SPOUSE') {
                addRelation(relativeRef, selfRef, relationType);
            }
            form.reset();

        })
        .catch(error => {
            console.error('ERROR:', error);
            alert('ERROR adding person. Please try again.');
        });

}