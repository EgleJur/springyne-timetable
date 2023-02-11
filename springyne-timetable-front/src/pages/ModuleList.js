import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function ModuleListPage() {
  const [modules, setModules] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const fetchModules = () => {
    fetch(
      `/api/v1/modules/search?name=${searchName}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setModules(jsonResponse));
  };

  useEffect(fetchModules, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/modules/search?name=${searchName}&page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setModules(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/modules/search?name=${searchName}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setModules(jsonResponse));
  };

  const deleteModule = (id) => {
    fetch(`/api/v1/modules/delete/` + id, {
      method: "PATCH",
    }).then(fetchModules);
    setDeleted(true);
    setRestored(false);
  };
  const restoreModule = (id) => {
    fetch(`/api/v1/modules/restore/` + id, {
      method: "PATCH",
    }).then(fetchModules);
    setDeleted(false);
    setRestored(true);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Moduliai</h2>
      <Collapse in={deleted}>
        <Alert
          onClose={() => {
            setDeleted(false);
          }}
          severity="info"
          className="mb-3"
        >
          Įrašas sėkmingai ištrintas
        </Alert>
      </Collapse>

      <Collapse in={restored}>
        <Alert
          onClose={() => {
            setRestored(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atstatytas
        </Alert>
      </Collapse>

      <div className="d-flex">
        <button className="btn btn-primary mb-5">
          <Link to="/modules/create" className="nav-link">
            Pridėti naują modulį
          </Link>
        </button>
      </div>
      <div className="d-flex justify-content-end">
        <div className="mb-4">
          <form className="d-flex" role="search">
            <label htmlFor="page-size-select" className="me-2">
              Puslapyje:
            </label>
            <Select
              id="page-size-select"
              value={pageSize}
              size="small"
              className="me-2"
              onChange={handlePageSizeChange}
            >
              <MenuItem value={10}>10</MenuItem>
              <MenuItem value={25}>25</MenuItem>
              <MenuItem value={50}>50</MenuItem>
              <MenuItem value={100}>100</MenuItem>
            </Select>
            <TextField
              onChange={(e) => setSearchName(e.target.value)}
              value={searchName}
              id="search-name-input"
              label="Ieškoti pavadinimo"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fetchModules}
            >
              Ieškoti
            </button>
          </form>
        </div>
        <div>
          <Pagination
            count={modules.totalPages}
            defaultPage={1}
            siblingCount={0}
            onChange={handlePageChange}
            value={page}
          />
        </div>
      </div>

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Numeris</th>
            <th>Pavadinimas</th>
            <th>Detalės</th>
            <th>Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {modules.content?.map((module) => (
            <tr
              key={module.id}
              id={module.id}
              className={module.deleted && "text-black-50"}
            >
              <td>{module.number}</td>
              <td>{module.name}</td>
              <td>{module.deleted ? "Modulis ištrintas" : ""}</td>
              <td>
                <button className="btn btn-outline-primary me-2 my-1">
                  <Link className="nav-link" to={"/modules/view/" + module.id}>
                    Žiūrėti
                  </Link>
                </button>
                <button className="btn btn-outline-primary me-2 my-1">
                  <Link className="nav-link" to={"/modules/edit/" + module.id}>
                    Redaguoti
                  </Link>
                </button>
                {module.deleted ? (
                  <button
                    className="btn btn-outline-secondary me-2 my-1"
                    onClick={() => restoreModule(module.id)}
                  >
                    Atstatyti
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-danger me-2 my-1"
                    onClick={() => deleteModule(module.id)}
                  >
                    Ištrinti
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ModuleListPage;
