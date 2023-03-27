import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Collapse, Alert, TextField } from "@mui/material";
import DeleteTwoToneIcon from "@mui/icons-material/DeleteTwoTone";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { apiUrl } from "../../App";

function HolidayListPage() {
  const [holidays, setHolidays] = useState([]);
  const [searchName, setSearchName] = useState("");
  const [deleted, setDeleted] = useState(false);
  const [startDateValue, setStartDateValue] = useState(null);
  const [endDateValue, setEndDateValue] = useState(null);
  const [startDateError, setStartDateError] = useState("");
  const [endDateError, setEndDateError] = useState("");


  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fetchHolidays = () => {
    setStartDateError(false);
    setEndDateError(false);
    if (!!startDateValue ^ !!endDateValue) {
      if (startDateValue === null) {
        setStartDateError(true);
      }
      if (endDateValue === null) {
        setEndDateError(true);
      }
    } else {
      const searchStartDate =
        startDateValue === "" || startDateValue === null
          ? ""
          : dayjs(startDateValue).format("YYYY-MM-DD");
      const searchEndDate =
        endDateValue === "" || endDateValue === null
          ? ""
          : dayjs(endDateValue).format("YYYY-MM-DD");
      fetch(
        `${apiUrl}/api/v1/holidays/search?name=${searchName}&from=${searchStartDate}&to=${searchEndDate}`
      )
        .then((response) => response.json())
        .then((jsonResponse) => setHolidays(jsonResponse));
    }
  };

  useEffect(fetchHolidays, []);

  const deleteHoliday = (id) => {
    fetch(`${apiUrl}/api/v1/holidays/delete/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchHolidays);
    setDeleted(true);
    setTimeout(() => {
      setDeleted(false);
    }, 5000);
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
          <button className="btn btn-primary mb-5 me-2">
            <Link to="/holidays/create" className="nav-link">
              Pridėti naujas atostogas
            </Link>
          </button>
        </div>
        <div className="mb-4">
          <form className="d-flex" role="search"></form>
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
            <DatePicker
              className="mb-3 me-2"
              label="Data nuo"
              value={startDateValue}
              maxDate={endDateValue}
              onChange={(newValue) => {
                setStartDateValue(newValue);
              }}
              renderInput={(params) => (
                <TextField
                  fullWidth
                  size="small"
                  {...params}
                  error={!!startDateError}
                />
              )}
            />
            <DatePicker
              className="mb-3 me-2"
              label="Data iki"
              value={endDateValue}
              minDate={startDateValue}
              onChange={(newValue) => {
                setEndDateValue(newValue);
              }}
              renderInput={(params) => (
                <TextField
                  fullWidth
                  size="small"
                  {...params}
                  error={!!endDateError}
                />
              )}
            />
            <button
              className="btn btn-outline-primary mb-3"
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

              <td className="text-end">
                <button
                  className="btn btn-danger me-2 my-1 btn-link"
                  title="Ištrinti"
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
              {holidays?.length == "0"
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
