import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function TeacherListPage() {
  const [teachers, setTeachers] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchSubject, setSearchSubject] = useState("");
  const [searchShift, setSearchShift] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const [subjects, setSubjects] = useState([]);
  const [shifts, setShifts] = useState([]);

  const fetchTeachers = () => {
    fetch(
      `/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  useEffect(fetchTeachers, []);

  useEffect(() => {
    fetch(`/api/v1/subjects`)
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  }, []);

  useEffect(() => {
    fetch(`/api/v1/shifts`)
      .then((response) => response.json())
      .then((jsonResponse) => setShifts(jsonResponse));
  }, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${
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
      `/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  const handleSearch = () => {
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${0}&pageSize=${pageSize}`
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
            Pridėti naują mokytoją
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
            <label htmlFor="select-subject" className="me-2">
              Dalykas:
            </label>
            <Select
              id="select-subject"
              size="small"
              className="me-2"
              fullWidth
              value={searchSubject}
              onChange={(e) => setSearchSubject(e.target.value)}
            >
              <MenuItem value={""}>-</MenuItem>
              {subjects?.map((subject) => (
                <MenuItem value={subject.id} key={subject.id}>
                  {subject.name}
                </MenuItem>
              ))}
            </Select>

            <label htmlFor="select-shift" className="me-2">
              Pamaina:
            </label>
            <Select
              id="select-shift"
              size="small"
              className="me-2"
              fullWidth
              value={searchShift}
              onChange={(e) => setSearchShift(e.target.value)}
            >
              <MenuItem value={""}>-</MenuItem>
              {shifts?.map((shift) => (
                <MenuItem value={shift.id} key={shift.id}>
                  {shift.name}
                </MenuItem>
              ))}
            </Select>

            <TextField
              onChange={(e) => setSearchName(e.target.value)}
              value={searchName}
              id="search-name-input"
              label="Ieškoti vardo"
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
            <th>Vardas ir Pavardė</th>
            <th>Dalykai</th>
            <th>Pamaina</th>
            <th>Būsena</th>
            <th>Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {teachers.content?.map((teacher) => (
            <tr
              key={teacher.id}
              id={teacher.id}
              className={teacher.deleted ? "text-black-50" : ""}
            >
              <td>{teacher.name}</td>
              <td>
                {teacher.subjects?.map((subject) => (
                  <div key={subject.id} id={subject.id}>
                    {subject.name}
                  </div>
                ))}
              </td>
              <td>{teacher.shift.name}</td>
              <td>{teacher.deleted ? "Mokytojas ištrintas" : ""}</td>
              <td>
                <button className="btn btn-outline-primary me-2 my-1">
                  <Link
                    className="nav-link"
                    to={"/teachers/view/" + teacher.id}
                  >
                    Žiūrėti
                  </Link>
                </button>
                <button
                  className="btn btn-outline-primary me-2 my-1"
                  disabled={teacher.deleted}
                >
                  <Link
                    className="nav-link"
                    to={"/teachers/edit/" + teacher.id}
                  >
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
        <tfoot className="table-light">
          <tr>
            <td colSpan={5}>
              {teachers.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${teachers.totalElements}`}
            </td>
          </tr>
        </tfoot>
      </table>
    </div>
  );
}

export default TeacherListPage;
