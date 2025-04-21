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

// Get CSRF token for request authorization
function getCSRF() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    return { csrfToken, csrfHeader };
}

// Get the logged-in user ID
function getLoggedUserId() {
    const loggedUserId = parseInt(document.querySelector('meta[name="_user_id"]').getAttribute('content'));
    return loggedUserId;
}

// Path for REST API endpoints
const apiPath = '/api';

// Add a new relation between two persons
function addRelation(person1, person2, relationType) {
    const relationData = {
        person1: person1,
        person2: person2,
        type: relationType,
        userId: getLoggedUserId()
    };

    const csrf = getCSRF();
    // Make REST request to add relation
    const addRelationUrl = apiPath + '/relations';
    fetch(addRelationUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrf.csrfHeader]: csrf.csrfToken
        },
        body: JSON.stringify(relationData)
    })
        .then(response => {
            if (!response.ok) {
                console.error(`ERROR: request to ${addRelationUrl} returned ${response.status}`);
                throw new Error('Cannot create relation');
            }
            return response.json();
        })
        .then(data => {
            console.log(`Successfully added person (id: ${data.id})`);
        })
        .catch(error => {
            console.error('ERROR:', error);
        });
}

// Add new person to the family tree
function addPerson() {
    const form = document.getElementById('addPersonForm');
    const formData = new FormData(form);

    const personData = {
        firstName: formData.get('firstName'),
        middleName: formData.get('middleName'),
        lastName: formData.get('lastName'),
        birthDate: formData.get('birthDate'),
        deathDate: formData.get('deathDate'),
        gender: formData.get('gender'),
        portraitUrl: formData.get('portraitUrl'),
        userId: getLoggedUserId(),
    };

    const csrf = getCSRF();
    // Make REST request to add person
    const addPersonPath = apiPath + '/persons'
    fetch(addPersonPath, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrf.csrfHeader]: csrf.csrfToken
        },
        body: JSON.stringify(personData)
    })
        .then(response => {
            if (!response.ok) {
                console.error(`ERROR: request to ${addPersonPath} returned ${response.status}`);
                throw new Error('Cannot add person');
            }
            return response.json();
        })
        .then(data => {
            console.log(`Successfully added person (id: ${data.id})`);

            // Get data for new relations
            const selfRef = data._links.self.href
            const relationType = formData.get('relationType');
            const relativeId = formData.get('relative');
            const relativeRef = 'api/persons/' + relativeId;

            // Add new relation for the new person
            if (relationType === 'CHILD') {
                addRelation(relativeRef, selfRef, relationType);
            } else if (relationType === 'PARENT') {
                addRelation(selfRef, relativeRef, 'CHILD');
            } else if (relationType === 'SPOUSE') {
                addRelation(relativeRef, selfRef, relationType);
            }
            form.reset();

            // Reload the page to see the new person
            window.location.href = '/fam-tree';
        })
        .catch(error => {
            console.error('ERROR:', error);
        });

}

// Set the root person for the family tree
function setRootPerson() {
    // Get the selected person ID from the dropdown
    const selection = document.getElementById('root-person-select-id');
    const selectedIndex = selection.selectedIndex;
    const rootPersonId = selection.options[selectedIndex].value;

    const csrf = getCSRF();

    // Make a request to set the root person
    const setRootPersonUrl = 'set-root-person?id=' + rootPersonId;
    fetch(setRootPersonUrl, {
        method: 'GET',
        headers: {
            [csrf.csrfHeader]: csrf.csrfToken
        }
    })
        .then(response => {
            if (!response.ok) {
                console.error(`ERROR: request to ${setRootPersonUrl} returned ${response.status}`);
                throw new Error('Cannot set root person');
            }

            // Reload the page to render the family tree with the new root
            window.location.reload();
        })
        .catch(error => {
            console.error('ERROR:', error);
        });
}