module.exports = class Edge {
    constructor(edge) {
        this.edge_Id = edge.edge_Id;//uuid
        this.startNodeId = edge.startNodeId;//uuid
        this.endNodeId = edge.endNodeId;//uuid
        this.distance = edge.distance;//numeric
        this.weight = edge.weight;//numeric
        this.name = edge.name;//text
        this.highwayType = edge.highwayType;//text
    }
    getEdge() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}


