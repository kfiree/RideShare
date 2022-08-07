module.exports = class Node {
    constructor(node) {
        this.osm_Id = node.osm_Id;//uuid
        this.node_Id = node.node_Id;//uuid
        this.latitude = node.latitude;//numeric
        this.longitude = node.longitude;//numeric
        this.degree = node.degree;//numeric
        this.edges = node.edges;//json
        this.tags = node.tags;//json
    }
    getNode() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}


