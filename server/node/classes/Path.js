module.exports = class Path {
    constructor(path) {
        this.path_Id = path.path_Id;//uuid
        this.edges = path.edges;//json
    }
    getPath() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}


