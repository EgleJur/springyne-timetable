import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";


function SubjectListPage() {
  const [subjects, setSubjects] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchModName, setSearchModName] = useState("");
  const [page, setPage] = useState(1);

  const JSON_HEADERS = {
    "Content-Type": "application/json"
  }
  const fetchSubjects = () => {
    fetch(`/api/v1/subjects/search?name=${searchName}&page=${pageNumber}&pageSize=${pageSize}`)
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));

  };

  useEffect(fetchSubjects, []);

  const fetchSubjectsByModules = () => {
    fetch(`/api/v1/subjects/byModule/search?name=${searchModName}&page=${pageNumber}&pageSize=${pageSize}`)
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));

  };

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/subjects/search?name=${searchName}&page=${value - 1}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/subjects/search?name=${searchName}&page=${0}&pageSize=${e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  };


  const deleteSubject = (id) => {
    fetch('/api/v1/subjects/delete/' + id, {
      method: 'PATCH',
      headers: JSON_HEADERS,
    })
      .then(fetchSubjects);
  };

  const restoreSubject = (id) => {
    fetch('/api/v1/subjects/restore/' + id, {
      method: 'PATCH',
      headers: JSON_HEADERS,
    })
      .then(fetchSubjects);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Dalykai</h2>
      

      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
        <button className="btn btn-primary mb-4">
          <Link to="/subjects/create" className="nav-link">
            Pridėti naują dalyką
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
              onClick={fetchSubjects}
            >
              Ieškoti
            </button>

          </form>
        </div>
      </div>
      <div className="d-flex justify-content-end">
        <div className="mb-4">
          <form className="d-flex" role="search">

            <TextField
              onChange={(e) => setSearchModName(e.target.value)}
              value={searchModName}
              id="search-name-input"
              label="Ieškoti pagal modulį"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fetchSubjectsByModules}
            >
              Ieškoti
            </button>

          </form>
        </div>
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

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Pavadinimas</th>
            <th>Modulis</th>
            <th>Būsena</th>
            <th>Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {subjects.content?.map((subject) => (
            <tr key={subject.id} id={subject.id}>
              <td>{subject.name}</td>
              <td>{subject.module?.name}</td>
              <td>{subject.deleted ? "Ištrintas" : ""}</td>
              <td>
                <button className="btn btn-outline-primary">
                  <Link className="nav-link" to={"/subjects/view/" + subject.id}>
                    Žiūrėti
                  </Link>
                </button>
                <button className="btn btn-outline-primary ms-2">
                  <Link className="nav-link" to={"/subjects/edit/" + subject.id}>
                    Redaguoti
                  </Link>
                </button>
                {subject.deleted ? (
                  <button
                    className="btn btn-outline-danger ms-2"
                    onClick={() => restoreSubject(subject.id)}>
                    Atstatyti
                  </button>
                ) : (

                  <button
                    className="btn btn-outline-danger ms-2"
                    onClick={() => deleteSubject(subject.id)}>
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

export default SubjectListPage;