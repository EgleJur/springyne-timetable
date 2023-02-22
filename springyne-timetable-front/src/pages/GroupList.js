import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function GroupListPage() {
  const [groups, setGroups] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchProgName, setSearchProgName] = useState("");
  const [searchYear, setSearchYear] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fetchGroups = () => {
    fetch(
      `/api/v1/groups/search?name=${searchName}&programName=${searchProgName}&groupYear=${searchYear}&page=${pageNumber}&pageSize=${pageSize}`)
      // `/api/v1/groups/search?name=${searchName}&programName=${searchProgName}&groupYear=${searchYear}&page=${pageNumber}&pageSize=${pageSize}`)
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  useEffect(fetchGroups, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/groups/search?name=${searchName}
      &programName=${searchProgName}&year=${searchYear}
      &page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `/api/v1/groups/search?name=${searchName}
      &programName=${searchProgName}&year=${searchYear}
      &page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  const deleteGroup = (id) => {
    fetch("/api/v1/groups/delete/" + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchGroups);
    setDeleted(true);
    setRestored(false);
  };

  const restoreGroup = (id) => {
    fetch("/api/v1/groups/restore/" + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchGroups);
    setDeleted(false);
    setRestored(true);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Grupės</h2>
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
          <button className="btn btn-primary mb-5">
            <Link to="/groups/create" className="nav-link">
              Pridėti naują Grupę
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
            <TextField
              onChange={(e) => setSearchProgName(e.target.value)}
              value={searchProgName}
              id="search-program-input"
              label="Ieškoti pagal programą"
              className="form-control me-2"
              size="small"
            />
            <TextField
              onChange={(e) => setSearchYear(e.target.value)}
              value={searchYear}
              id="search-year-input"
              label="Ieškoti pagal metus"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fetchGroups}
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
            <th>Programa</th>
            <th>Metai</th>
            <th>Studentai</th>
            <th>Būsena</th>
            <th className="d-flex justify-content-center">Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {groups.content?.map((group) => (
            <tr key={group.id} 
            id={group.id}
              className={group.deleted ? "text-black-50" : ""}>
              <td>{group.name}</td>
              <td>{group.program?.name}</td>
              <td>{group.groupYear}</td>
              <td>{group.students}</td>
              <td>{group.deleted ? "Ištrintas" : ""}</td>
              <td className="d-flex justify-content-end">
                <button className="btn btn-outline-primary me-2 my-1 ">
                  <Link
                    className="nav-link"
                    to={"/groups/view/" + group.id}
                  >
                    Žiūrėti
                  </Link>
                </button>

                <button
                  className="btn btn-outline-primary me-2 my-1"
                  disabled={group.deleted}
                >
                  <Link
                    className="nav-link"
                    to={"/groups/edit/" + group.id}
                  >
                    Redaguoti
                  </Link>
                </button>

                {group.deleted ? (
                  <button
                    className="btn btn-outline-danger ms-2 my-1"
                    onClick={() => restoreGroup(group.id)}
                  >
                    Atstatyti
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-danger ms-2 my-1"
                    onClick={() => deleteGroup(group.id)}
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
            <td colSpan={6}>
              {groups.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${groups.totalElements}`}
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
            count={groups.totalPages}
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

export default GroupListPage;
