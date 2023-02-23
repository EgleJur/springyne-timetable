import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FormControl, TextField } from "@mui/material";
import { Select, MenuItem, Pagination, InputLabel } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function ProgramListPage() {
  const [programs, setPrograms] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const fetchPrograms = () => {
    fetch(
      `/api/v1/programs/search?name=${searchName}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setPrograms(jsonResponse));
  };

  useEffect(fetchPrograms, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/programs/search?name=${searchName}&page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setPrograms(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/programs/search?name=${searchName}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setPrograms(jsonResponse));
  };

  const handleSearch = () => {
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/programs/search?name=${searchName}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setPrograms(jsonResponse));
  };

  const deleteProgram = (id) => {
    fetch(`/api/v1/programs/delete/` + id, {
      method: "PATCH",
    }).then(fetchPrograms);
    setDeleted(true);
    setRestored(false);
  };
  const restoreProgram = (id) => {
    fetch(`/api/v1/programs/restore/` + id, {
      method: "PATCH",
    }).then(fetchPrograms);
    setDeleted(false);
    setRestored(true);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Programos</h2>
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
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-4 me-2">
            <Link to="/programs/create" className="nav-link">
              Pridėti naują programą
            </Link>
          </button>
        </div>
        <div className="mb-4">
          <form className="d-flex" role="search">
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
              onClick={handleSearch}
            >
              Ieškoti
            </button>
          </form>
        </div>
      </div>

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Pavadinimas</th>
            <th>Aprašymas</th>
            <th>Būsena</th>
            <th className="d-flex text-start">
              <div className="ms-0 ms-sm-2 ms-md-3 ms-lg-4">Veiksmai</div>
            </th>
          </tr>
        </thead>
        <tbody>
          {programs.content?.map((program) => (
            <tr
              key={program.id}
              id={program.id}
              className={program.deleted ? "text-black-50" : ""}
            >
              <td>{program.name}</td>
              <td>{program.description}</td>
              <td>{program.deleted ? "Ištrintas" : ""}</td>
              <td className="justify-content-end text-end">
                <button className="btn btn-outline-primary me-2 my-1">
                  <Link
                    className="nav-link"
                    to={"/programs/view/" + program.id}
                  >
                    Žiūrėti
                  </Link>
                </button>
                <button
                  className="btn btn-outline-primary me-2 my-1"
                  disabled={program.deleted}
                >
                  <Link
                    className="nav-link"
                    to={"/programs/edit/" + program.id}
                  >
                    Redaguoti
                  </Link>
                </button>
                {program.deleted ? (
                  <button
                    className="btn btn-outline-secondary my-1 me-2 me-xl-0"
                    onClick={() => restoreProgram(program.id)}
                  >
                    Atstatyti
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-danger me-2 me-xl-3 my-1"
                    onClick={() => deleteProgram(program.id)}
                  >
                    Ištrinti
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot className="table-light">
          <tr>
            <td>
              {programs.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${programs.totalElements}`}
            </td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </tfoot>
      </table>

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
          </form>
        </div>
        <div>
          <Pagination
            count={programs.totalPages}
            defaultPage={1}
            siblingCount={0}
            onChange={handlePageChange}
            value={page}
          />
        </div>
      </div>
    </div>
  );
}

export default ProgramListPage;
