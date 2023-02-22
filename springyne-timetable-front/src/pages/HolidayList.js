import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";

function HolidayListPage() {
  const [holidays, setHolidays] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchBuinding, setSearchDate] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);


  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fetchHolidays = () => {
    fetch(
      `/api/v1/holidays`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setHolidays(jsonResponse));
  };

  useEffect(fetchHolidays, []);

  const fetchHolidaysByBuildings = () => {
    fetch(
      `/api/v1/holidays/`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setHolidays(jsonResponse));
  };

  
  const deleteHoliday = (id) => {
    fetch("/api/v1/holidays/delete/" + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchHolidays);
    setDeleted(true);
    setRestored(false);
  };

  const restoreHoliday = (id) => {
    fetch("/api/v1/holidays/restore/" + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchHolidays);
    setDeleted(false);
    setRestored(true);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Atostogos</h2>
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
          <button className="btn btn-primary mb-4">
            <Link to="/holidays/create" className="nav-link">
              Pridėti naujas atostogas
            </Link>
          </button>
        </div>
        <div className="mb-4">
          <form className="d-flex" role="search">
            
          </form>
        </div>
      </div>
      <div className="d-flex justify-content-end">
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
              onChange={(b) => setSearchDate(b.target.value)}
              value={searchBuinding}
              id="search-date-input"
              label="Ieškoti pagal datą"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fetchHolidays}
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
            <th>Data nuo</th>
            <th>Data iki</th>
            <th>Būsena</th>
            <th className="d-flex justify-content-center">Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {holidays.map((holiday) => (
            <tr key={holiday.id} id={holiday.id}>
              <td>{holiday.name}</td>
              <td>{holiday.yearStarts}-{holiday.monthStarts}-{holiday.dayStarts}</td>
              <td>{holiday.yearEnds}-{holiday.monthEnds}-{holiday.dayEnds}</td>
              <td>{holiday.deleted ? "Ištrintas" : ""}</td>
              <td className="d-flex justify-content-end">
                
                <button
                  className="btn btn-outline-primary ms-2"
                  disabled={holiday.deleted}
                >
                  <Link className="nav-link" to={"/holidays/edit/" + holiday.id}>
                    Redaguoti
                  </Link>
                </button>

                {holiday.deleted ? (
                  <button
                    className="btn btn-outline-danger ms-2"
                    onClick={() => restoreHoliday(holiday.id)}
                  >
                    Atstatyti
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-danger ms-2"
                    onClick={() => deleteHoliday(holiday.id)}
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
              {holidays.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${holidays.totalElements}`}
            </td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </tfoot>
      </table>
      
    </div>
  );
}

export default HolidayListPage;
