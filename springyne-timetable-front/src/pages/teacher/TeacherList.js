import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Select, MenuItem, Pagination, TextField, Collapse, Alert } from "@mui/material";
import EditTwoToneIcon from '@mui/icons-material/EditTwoTone';
import DeleteTwoToneIcon from '@mui/icons-material/DeleteTwoTone';
import RestoreTwoToneIcon from '@mui/icons-material/RestoreTwoTone';
import VisibilityTwoToneIcon from '@mui/icons-material/VisibilityTwoTone';
import { apiUrl } from "../../App";


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
      `${apiUrl}/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  useEffect(fetchTeachers, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/subjects`)
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  }, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/shifts`)
      .then((response) => response.json())
      .then((jsonResponse) => setShifts(jsonResponse));
  }, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `${apiUrl}/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${value - 1
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
      `${apiUrl}/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${0}&pageSize=${e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/teachers/search?name=${searchName}&shiftId=${searchShift}&subjectId=${searchSubject}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  const deleteTeacher = (id) => {
    fetch(`${apiUrl}/api/v1/teachers/delete/` + id, {
      method: "PATCH",
    }).then(fetchTeachers);
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
    }, 5000);
  };
  const restoreTeacher = (id) => {
    fetch(`${apiUrl}/api/v1/teachers/restore/` + id, {
      method: "PATCH",
    }).then(fetchTeachers);
    setDeleted(false);
    setRestored(true);
    setTimeout(() => {
      setRestored(false);
    }, 5000);
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
      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-5 me-2">
            <Link to="/teachers/create" className="nav-link">
              Pridėti naują mokytoją
            </Link>
          </button>
        </div>


        <div className="mb-4">
          <form className="d-flex" role="search">
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
              style={{ maxWidth: '150px' }}
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
              style={{ maxWidth: '150px' }}
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
              label="Ieškoti pagal vardą"
              className="form-control me-2 search-name-input"
              size="small"
              style={{ width: '400px' }}
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
            <th>Vardas ir Pavardė</th>
            <th>Dalykai</th>
            <th>Pamaina</th>
            <th>Būsena</th>
            <th ></th>
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
              <td>{teacher.deleted ? "Ištrintas" : ""}</td>
              <td className="text-end">
                <div className="ms-5">
                  <button className="btn btn-outline-primary me-1 my-1 btn-link" title="Žiūrėti">
                    <Link
                      className="nav-link"
                      to={"/teachers/view/" + teacher.id}
                    >
                      <VisibilityTwoToneIcon />
                    </Link>
                  </button>
                  <button
                    className="btn btn-outline-primary me-1 my-1 btn-link" title="Redaguoti"
                    disabled={teacher.deleted}
                  >
                    <Link
                      className="nav-link"
                      to={"/teachers/edit/" + teacher.id}
                    >
                      <EditTwoToneIcon />
                    </Link>
                  </button>
                  {teacher.deleted ? (
                    <button
                      className="btn btn-outline-secondary me-1 my-1 btn-link" title="Atstatyti"
                      onClick={() => restoreTeacher(teacher.id)}
                    >
                      <RestoreTwoToneIcon />
                    </button>
                  ) : (
                    <button
                      className="btn btn-danger me-2 my-1 btn-link" title="Ištrinti"
                      onClick={() => deleteTeacher(teacher.id)}
                    >
                      <DeleteTwoToneIcon className="red-icon" />
                    </button>
                  )}
                </div>
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
            count={teachers.totalPages}
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

export default TeacherListPage;
