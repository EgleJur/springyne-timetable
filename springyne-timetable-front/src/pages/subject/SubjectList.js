import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Select, MenuItem, Pagination, TextField, Collapse, Alert } from "@mui/material";
import EditTwoToneIcon from '@mui/icons-material/EditTwoTone';
import DeleteTwoToneIcon from '@mui/icons-material/DeleteTwoTone';
import RestoreTwoToneIcon from '@mui/icons-material/RestoreTwoTone';
import VisibilityTwoToneIcon from '@mui/icons-material/VisibilityTwoTone';
import { apiUrl } from "../../App";


function SubjectListPage() {
  const [subjects, setSubjects] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchModuleName, setSearchModuleName] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fetchSubjects = () => {
    fetch(
      `${apiUrl}/api/v1/subjects/search?name=${searchName}&moduleName=${searchModuleName}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };

  useEffect(fetchSubjects, []);


  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `${apiUrl}/api/v1/subjects/search?name=${searchName}&moduleName=${searchModuleName}&page=${value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/subjects/search?name=${searchName}&moduleName=${searchModuleName}&page=${0}&pageSize=${e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/subjects/search?name=${searchName}&moduleName=${searchModuleName}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };

  const deleteSubject = (id) => {
    fetch(`${apiUrl}/api/v1/subjects/delete/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchSubjects);
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
    }, 5000);
  };

  const restoreSubject = (id) => {
    fetch(`${apiUrl}/api/v1/subjects/restore/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchSubjects);
    setDeleted(false);
    setRestored(true);
    setTimeout(() => {
      setRestored(false);
    }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Dalykai</h2>
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

      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-5 me-2">
            <Link to="/subjects/create" className="nav-link">
              Pridėti naują dalyką
            </Link>
          </button>
        </div>
        <div className="mb-4">
          <form className="d-flex" role="search">
            <TextField
              onChange={(e) => {setSearchName(e.target.value)}}
              value={searchName}
              id="search-name-input"
              label="Ieškoti pagal pavadinimą"
              className="form-control me-2"
              size="small"
            />
            <TextField
              onChange={(e) => {setSearchModuleName(e.target.value)}}
              value={searchModuleName}
              id="search-module-input"
              label="Ieškoti pagal modulį"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={(e) => handleSearch(e)}
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
            <th>Modulis</th>
            <th>Būsena</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {subjects.content?.map((subject) => (
            <tr key={subject.id}
              id={subject.id}
              className={subject.deleted ? "text-black-50" : ""}>
              <td>{subject.name}</td>
              <td>{subject.module?.name}</td>
              <td>{subject.deleted ? "Ištrintas" : ""}</td>
              <td className="text-end">
                <button className="btn btn-outline-primary me-1 my-1 btn-link" title="Žiūrėti">
                  <Link
                    className="nav-link"
                    to={"/subjects/view/" + subject.id}
                  >
                    <VisibilityTwoToneIcon />
                  </Link>
                </button>

                <button
                  className="btn btn-outline-primary me-1 my-1 btn-link" title="Redaguoti"
                  disabled={subject.deleted}
                >
                  <Link
                    className="nav-link"
                    to={"/subjects/edit/" + subject.id}
                  >
                    <EditTwoToneIcon />
                  </Link>
                </button>

                {subject.deleted ? (
                  <button
                    className="btn btn-outline-secondary me-1 my-1 btn-link" title="Atstatyti"
                    onClick={() => restoreSubject(subject.id)}
                  >
                    <RestoreTwoToneIcon />
                  </button>
                ) : (
                  <button
                    className="btn btn-danger me-2 my-1 btn-link" title="Ištrinti"
                    onClick={() => deleteSubject(subject.id)}
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
            <td colSpan={5}>
              {subjects.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${subjects.totalElements}`}
            </td>
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
            count={subjects.totalPages}
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

export default SubjectListPage;
