import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FormControl, TextField } from "@mui/material";
import { Select, MenuItem, Pagination, InputLabel } from "@mui/material";
import { Collapse, Alert } from "@mui/material";
import EditTwoToneIcon from '@mui/icons-material/EditTwoTone';
import DeleteTwoToneIcon from '@mui/icons-material/DeleteTwoTone';
import RestoreTwoToneIcon from '@mui/icons-material/RestoreTwoTone';
import VisibilityTwoToneIcon from '@mui/icons-material/VisibilityTwoTone';
import { apiUrl } from "../../App";

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
      `${apiUrl}/api/v1/programs/search?name=${searchName}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setPrograms(jsonResponse));
  };

  useEffect(fetchPrograms, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `${apiUrl}/api/v1/programs/search?name=${searchName}&page=${
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
      `${apiUrl}/api/v1/programs/search?name=${searchName}&page=${0}&pageSize=${
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
      `${apiUrl}/api/v1/programs/search?name=${searchName}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setPrograms(jsonResponse));
  };

  const deleteProgram = (id) => {
    fetch(`${apiUrl}/api/v1/programs/delete/` + id, {
      method: "PATCH",
    }).then(fetchPrograms);
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
             }, 5000);
  };
  const restoreProgram = (id) => {
    fetch(`${apiUrl}/api/v1/programs/restore/` + id, {
      method: "PATCH",
    }).then(fetchPrograms);
    setDeleted(false);
    setRestored(true);
    setTimeout(() => {
      setRestored(false);
             }, 5000);
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
          <button className="btn btn-primary mb-5 me-2">
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
              label="Ieškoti pagal pavadinimą"
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
            <th>
              <div></div>
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
              <td className="text-end">
                <button className="btn btn-outline-primary me-1 my-1 btn-link" title="Žiūrėti">
                  <Link
                    className="nav-link"
                    to={"/programs/view/" + program.id}
                  >
                    <VisibilityTwoToneIcon/>
                  </Link>
                </button>
                <button
                  className="btn btn-outline-primary me-1 my-1 btn-link" title="Redaguoti"
                  disabled={program.deleted}
                >
                  <Link
                    className="nav-link"
                    to={"/programs/edit/" + program.id}
                  >
                    <EditTwoToneIcon/>
                  </Link>
                </button>
                {program.deleted ? (
                  <button
                  className="btn btn-outline-secondary me-1 my-1 btn-link" title="Atstatyti"
                    onClick={() => restoreProgram(program.id)}
                  >
                    <RestoreTwoToneIcon/>
                  </button>
                ) : (
                  <button
                  className="btn btn-danger me-2 my-1 btn-link" title="Ištrinti"
                    onClick={() => deleteProgram(program.id)}
                  >
                    <DeleteTwoToneIcon className="red-icon" />
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
