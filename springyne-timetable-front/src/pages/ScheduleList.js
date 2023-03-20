import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { Select, MenuItem, Pagination, InputLabel } from "@mui/material";
import { Collapse, Alert } from "@mui/material";
import EditTwoToneIcon from "@mui/icons-material/EditTwoTone";
import DeleteTwoToneIcon from "@mui/icons-material/DeleteTwoTone";
import EventTwoToneIcon from "@mui/icons-material/EventTwoTone";
import { apiUrl } from "../App";

function ScheduleListPage() {
  const [schedules, setSchedules] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const today = dayjs();
  const [searchDate, setSearchDate] = useState(today);
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleteScheduleId, setDeleteScheduleId] = useState(null);

  const formatSearchDate = () => {
    return searchDate == "" || searchDate == null
      ? ""
      : dayjs(searchDate).format("YYYY-MM-DD");
  };

  const fetchSchedules = () => {
    fetch(
      `${apiUrl}/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSchedules(jsonResponse));
  };

  useEffect(fetchSchedules, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `${apiUrl}/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSchedules(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSchedules(jsonResponse));
  };

  const handleSearch = () => {
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSchedules(jsonResponse));
  };

  const deleteSchedule = (id) => {
    fetch(`${apiUrl}/api/v1/schedules/delete/` + id, {
      method: "DELETE",
    }).then(fetchSchedules);
    setDeleted(true);
    setTimeout(() => {
      setDeleted(false);
    }, 5000);
  };

  const handleDeleteClick = (id) => {
    setDeleteScheduleId(id);
    setConfirmDelete(true);
  };

  const handleDeleteConfirm = () => {
    deleteSchedule(deleteScheduleId);
    setConfirmDelete(false);
  };

  const handleDeleteCancel = () => {
    setConfirmDelete(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Tvarkaraščiai</h2>

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

      <Collapse in={confirmDelete}>
        <Alert
          severity="warning"
          action={
            <>
              <button
                onClick={handleDeleteConfirm}
                className="btn btn-danger me-2"
              >
                Ištrinti
              </button>
              <button
                onClick={handleDeleteCancel}
                className="btn btn-secondary"
              >
                Atšaukti
              </button>
            </>
          }
        >
          <div style={{ fontSize: '1.2em' }} className="confirmation-message">Ar tikrai norite ištrinti tvarkaraštį?</div>
        </Alert>
      </Collapse>

      <br></br>

      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-5 me-2">
            <Link to="/schedules/create" className="nav-link">
              Pridėti naują tvarkaraštį
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
            <DatePicker
              className="mb-3 me-2"
              label="Ieškoti pagal datą"
              value={searchDate}
              onChange={(newValue) => {
                console.log(dayjs(newValue).format("YYYY-MM-DD"));
                setSearchDate(newValue);
              }}
              renderInput={(params) => (
                <TextField fullWidth size="small" {...params} />
              )}
            />
            <button
              className="btn btn-outline-primary mb-3"
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
            <th>Pradžios data</th>
            <th>Pabaigos data</th>
            <th>Būsena</th>
            <th>
              <div></div>
            </th>
          </tr>
        </thead>
        <tbody>
          {schedules.content?.map((schedule) => (
            <tr key={schedule.id} id={schedule.id}>
              <td>{schedule.name}</td>
              <td>{schedule.startDate}</td>
              <td>{schedule.endDate}</td>
              <td></td>
              <td className="text-end">
                <button
                  className="btn btn-outline-primary me-1 my-1 btn-link"
                  title="Planuoti tvarkaraštį"
                >
                  <Link
                    className="nav-link"
                    to={"/schedules/plan/" + schedule.id}
                  >
                    <EventTwoToneIcon />
                    {/* <EditTwoToneIcon /> */}
                  </Link>
                </button>

                <button
                  onClick={() => handleDeleteClick(schedule.id)}
                  className="btn btn-outline-danger me-1 my-1 btn-link"
                >
                  <DeleteTwoToneIcon className="red-icon"/>
                </button>
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot className="table-light">
          <tr>
            <td colSpan={5}>
              {schedules?.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${schedules?.totalElements}`}
            </td>
          </tr>
        </tfoot>{" "}
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
            count={schedules.totalPages}
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

export default ScheduleListPage;
