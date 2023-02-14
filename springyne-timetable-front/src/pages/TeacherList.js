import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function TeacherListPage() {
  const [teachers, setTeachers] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchLastname, setSearchLastname] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const fetchTeachers = () => {
    fetch(
      `/api/v1/teachers/search?name=${searchName}&lastname=${searchLastname}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  useEffect(fetchTeachers, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/teachers/search?name=${searchName}&lastname=${searchLastname}&page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/teachers/search?name=${searchName}&lastname=${searchLastname}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  const deleteTeacher = (id) => {
    fetch(`/api/v1/teachers/delete/` + id, {
      method: "PATCH",
    }).then(fetchTeachers);
    setDeleted(true);
    setRestored(false);
  };
  const restoreTeacher = (id) => {
    fetch(`/api/v1/teachers/restore/` + id, {
      method: "PATCH",
    }).then(fetchTeachers);
    setDeleted(false);
    setRestored(true);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Mokytojai</h2>
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
          <Link to="/teachers/create" className="nav-link">
            Pridėti naują mokytoja
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
              label="Ieškoti pagal Varda"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fetchTeachers}
            >
              Ieškoti
            </button>
          </form>
        </div>
        <div>
          <Pagination
            count={teachers.totalPages}
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
            <th>Vardas</th>
            <th>Pavardė</th>
            <th>Detalės</th>
            <th>Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {teachers.content?.map((teacher) => (
            <tr
              key={teacher.id}
              id={teacher.id}
              className={teacher.deleted && "text-black-50"}
            >
              <td>{teacher.number}</td>
              <td>{teacher.name}</td>
              <td>{teacher.lastname}</td>
              <td>{teacher.deleted ? "Mokytojas ištrintas" : ""}</td>
              <td>
                <button className="btn btn-outline-primary me-2 my-1">
                  <Link className="nav-link" to={"/teachers/view/" + teacher.id}>
                    Žiūrėti
                  </Link>
                </button>
                <button className="btn btn-outline-primary me-2 my-1">
                  <Link className="nav-link" to={"/teachers/edit/" + teacher.id}>
                    Redaguoti
                  </Link>
                </button>
                {teacher.deleted ? (
                  <button
                    className="btn btn-outline-secondary me-2 my-1"
                    onClick={() => restoreTeacher(teacher.id)}
                  >
                    Atstatyti
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-danger me-2 my-1"
                    onClick={() => deleteTeacher(teacher.id)}
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

export default TeacherListPage;
