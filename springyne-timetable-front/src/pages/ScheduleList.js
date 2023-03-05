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

function ScheduleListPage() {
  const [schedules, setSchedules] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const today = dayjs();
  const [searchDate, setSearchDate] = useState(today);
  const [page, setPage] = useState(1);

  const formatSearchDate = () => {
    return searchDate == "" || searchDate == null
      ? ""
      : dayjs(searchDate).format("YYYY-MM-DD");
  };

  const fetchSchedules = () => {
    fetch(
      `/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()
      }&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSchedules(jsonResponse));
  };

  useEffect(fetchSchedules, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${
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
      `/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${0}&pageSize=${
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
      `/api/v1/schedules/search?name=${searchName}&date=${formatSearchDate()}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setSchedules(jsonResponse));
  };

  //  const deleteSchedule = (id) => {
  //    fetch(`/api/v1/schedules/delete/` + id, {
  //      method: "PATCH",
  //    }).then(fetchSchedules);
  //    setDeleted(true);
  //    setTimeout(() => {
  //      setDeleted(false);
  //    }, 5000);
  //  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Tvarkaraščiai</h2>

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
              disablePast
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
                  className="btn btn-danger me-2 my-1 btn-link"
                  title="Ištrinti"
                  // onClick={() => deleteSchedule(schedule.id)}
                >
                  <DeleteTwoToneIcon className="red-icon" />
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
