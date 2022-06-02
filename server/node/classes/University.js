module.exports = class University {

    constructor(university) {
        this.university_Id = university.university_Id;
        this.university_name = university.university_name;
        this.geoLocation_Id = university.geoLocation_Id;
    }

    getUniversity() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}


