import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";
import DeleteTwoToneIcon from '@mui/icons-material/DeleteTwoTone';

function HolidayListPage() {
  const [holidays, setHolidays] = useState([]);
  const [searchName, setSearchName] = useState("");
  const [searchStartDate, setSearchStartDate] = useState("");
  const [searchEndDate, setSearchEndDate] = useState("");
  const [deleted, setDeleted] = useState(false);
  const [dateError, setDateError] = useState("");


  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fetchHolidays = (e) => {
    if (e && e.preventDefault) { e.preventDefault(); }
    setDateError(false);
    if (searchStartDate != "" && searchEndDate === ""
      || searchStartDate === "" && searchEndDate != ""
      || searchStartDate > searchEndDate 
      || searchStartDate != "" && isNaN(new Date(searchStartDate)) 
      || searchEndDate != "" && isNaN(new Date(searchEndDate))) {
      setDateError(true);
    } else {
      fetch(
        `/api/v1/holidays/search?name=${searchName}&from=${searchStartDate}&to=${searchEndDate}`
      )
        .then((response) => response.json())
        .then((jsonResponse) => setHolidays(jsonResponse))

    }
  };

  useEffect(fetchHolidays, []);

  const deleteHoliday = (id) => {
    fetch("/api/v1/holidays/delete/" + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchHolidays);
    setDeleted(true);
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

      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-5">
            <Link to="/holidays/create" className="nav-link">
              Pridėti naujas atostogas
            </Link>
          </button>
        </div>
        <div className="mb-4">
          <form className="d-flex" role="search">

          </form>
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
              error={!!dateError}
              onChange={(b) => setSearchStartDate(b.target.value)}
              value={searchStartDate}
              id="search-date-from-input"
              label="Data nuo (MMMM-MM-DD)"
              className="form-control me-2"
              size="small"
              required={searchEndDate}
            />

            <TextField
              error={!!dateError}
              onChange={(b) => setSearchEndDate(b.target.value)}
              value={searchEndDate}
              id="search-date-to-input"
              label="Data iki (MMMM-MM-DD)"
              className="form-control me-2"
              size="small"
              required={searchStartDate}
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
            <th></th>
          </tr>
        </thead>
        <tbody>
          {holidays?.map((holiday) => (

            <tr key={holiday.id} id={holiday.id}>
              <td>{holiday.name}</td>
              <td>{holiday.starts}</td>
              <td>{holiday.ends}</td>

              <td className="d-flex justify-content-end">

                <button
                  className="btn btn-danger me-2 my-1 btn-link" title="Ištrinti"
                  onClick={() => deleteHoliday(holiday.id)}
                >
                  <DeleteTwoToneIcon className="red-icon" />
                </button>

              </td>
            </tr>
          ))}
        </tbody>
        <tfoot className="table-light">
          <tr>
            <td colSpan={4}>
              {/* {count} */}
              {holidays.length == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${holidays.length}`}
            </td>
          </tr>
        </tfoot>
      </table>

    </div>
  );
}

export default HolidayListPage;
