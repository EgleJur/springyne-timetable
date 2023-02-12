// import { useState, useEffect } from "react";
// import { Link } from "react-router-dom";
// import { TextField } from "@mui/material";
// import { Select, MenuItem, Pagination } from "@mui/material";

// function ModuleListPage() {
//   const [modules, setModules] = useState({});
//   const [pageNumber, setPageNumber] = useState(0);
//   const [pageSize, setPageSize] = useState(20);
//   const [searchName, setSearchName] = useState("");
//   const [page, setPage] = useState(1);

//   const fetchModules = () => {fetch(
//     `/api/v1/modules/search?name=${searchName}&page=${pageNumber}&pageSize=${pageSize}`
//   )
//     .then((response) => response.json())
//     .then((jsonResponse) => setModules(jsonResponse))};

//   useEffect(fetchModules, []);

//   const handlePageChange = (e, value) => {
//     setPage(value);
//     setPageNumber(value - 1);
//     fetch(
//     `/api/v1/modules/search?name=${searchName}&page=${value-1}&pageSize=${pageSize}`
//   )
//     .then((response) => response.json())
//     .then((jsonResponse) => setModules(jsonResponse))};

//   const handlePageSizeChange = (e) => {
//     setPageSize(e.target.value);
//     setPage(1);
//     setPageNumber(0);
//     fetch(
//     `/api/v1/modules/search?name=${searchName}&page=${0}&pageSize=${e.target.value}`
//   )
//     .then((response) => response.json())
//     .then((jsonResponse) => setModules(jsonResponse))
//   };

//   return (
//     <div className="mx-3">
//       <h2 className="my-5">Moduliai</h2>
//       <div className="d-flex">
//         <button className="btn btn-primary mb-5">
//           <Link to="/modules/create" className="nav-link">
//             Pridėti naują modulį
//           </Link>
//         </button>
//       </div>
//       <div className="d-flex justify-content-end">
//         <div className="mb-4">
//           <form className="d-flex" role="search">
//             <label htmlFor="page-size-select" className="me-2">
//               Puslapyje:
//             </label>
//             <Select
//               id="page-size-select"
//               value={pageSize}
//               size="small"
//               className="me-2"
//               onChange={handlePageSizeChange}
//             >
//               <MenuItem value={5}>5</MenuItem>
//               <MenuItem value={10}>10</MenuItem>
//               <MenuItem value={20}>20</MenuItem>
//               <MenuItem value={50}>50</MenuItem>
//             </Select>
//             <TextField
//               onChange={(e) => setSearchName(e.target.value)}
//               value={searchName}
//               id="search-name-input"
//               label="Ieškoti pavadinimo"
//               className="form-control me-2"
//               size="small"
//             />
//             <button
//               className="btn btn-outline-primary"
//               type="submit"
//               onClick={fetchModules}
//             >
//               Ieškoti
//             </button>
//           </form>
//         </div>
//         <div>
//           <Pagination
//             count={modules.totalPages}
//             defaultPage={1}
//             siblingCount={0}
//             onChange={handlePageChange}
//             value={page}
//           />
//         </div>
//       </div>

//       <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
//         <thead className="table-light">
//           <tr>
//             <th>Numeris</th>
//             <th>Pavadinimas</th>
//             <th>Detalės</th>
//             <th>Veiksmai</th>
//           </tr>
//         </thead>
//         <tbody>
//           {modules.content?.map((module) => (
//             <tr key={module.id} id={module.id}>
//               <td>{module.number}</td>
//               <td>{module.name}</td>
//               <td>{module.deleted ? "Modulis ištrintas" : ""}</td>
//               <td>
//                 <button className="btn btn-outline-primary me-2 my-1">
//                   Žiūrėti
//                 </button>
//                 <button className="btn btn-outline-primary me-2 my-1">
//                   Redaguoti
//                 </button>
//                 {module.deleted ? (
//                   <button className="btn btn-outline-danger me-2 my-1">
//                     Atstatyti
//                   </button>
//                 ) : (
//                   <button className="btn btn-outline-danger me-2 my-1">
//                     Ištrinti
//                   </button>
//                 )}
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   );
// }

// export default ModuleListPage;
