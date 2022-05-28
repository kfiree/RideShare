
const pathQuerys = {
    addPath: (path) => {
        return `INSERT INTO "public"."rs_paths" 
                ("path_Id", "edges") 
                VALUES 
                ('${path.path_Id}', '${path.edges}');`;
    },
    updatePath: (path) => {
        return `UPDATE "public"."rs_paths" SET 
                "edges" = '${path.edges}' 
                WHERE "path_Id" = '${path.path_Id}';
        `;
    },
    deletePathById: (path) => {
        return `DELETE FROM "public"."rs_paths" 
                WHERE "path_Id" = '${path.path_Id}';`;
    },
    getPathById: (path) => {
        return `SELECT * FROM "public"."rs_paths" 
                WHERE "path_Id" = '${path.path_Id}';`;
    },
    getAllUPaths: () => { return `SELECT * FROM "public"."rs_paths";`; },
}

export default pathQuerys;
