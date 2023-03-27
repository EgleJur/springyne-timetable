import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { TextField } from "@mui/material";
import { Select, MenuItem, Pagination } from "@mui/material";
import { Collapse, Alert } from "@mui/material";
import EditTwoToneIcon from '@mui/icons-material/EditTwoTone';
import DeleteTwoToneIcon from '@mui/icons-material/DeleteTwoTone';
import RestoreTwoToneIcon from '@mui/icons-material/RestoreTwoTone';
import VisibilityTwoToneIcon from '@mui/icons-material/VisibilityTwoTone';
import { apiUrl } from "../../App";


function RoomListPage() {
  const [rooms, setRooms] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchBuinding, setSearchBuilding] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fectchRooms = () => {
    fetch(
      `${apiUrl}/api/v1/rooms/search?name=${searchName}&building=${searchBuinding}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setRooms(jsonResponse));
  };

  useEffect(fectchRooms, []);

  const fectchRoomsByBuildings = () => {
    fetch(
      `${apiUrl}/api/v1/rooms/searchByBuilding?building=${searchBuinding}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setRooms(jsonResponse));
  };

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `${apiUrl}/api/v1/rooms/searchByName?name=${searchName}&page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setRooms(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/rooms/searchByName?name=${searchName}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setRooms(jsonResponse));
  };

  const deleteRoom = (id) => {
    fetch(`${apiUrl}/api/v1/rooms/delete/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fectchRooms);
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
             }, 5000);
  };

  const restoreRoom = (id) => {
    fetch(`${apiUrl}/api/v1/rooms/restore/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fectchRooms);
    setDeleted(false);
    setRestored(true);
    setTimeout(() => {
      setDeleted(setRestored);
             }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Kabinetai</h2>
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
            <Link to="/rooms/create" className="nav-link">
              Pridėti naują kabinetą
            </Link>
          </button>
        
      </div>
      
        <div className="mb-4">
          <form className="d-flex" role="search">
          <TextField
              onChange={(e) => {setSearchName(e.target.value); setPageNumber(0); setPage(1);}}
              value={searchName}
              id="search-name-input"
              label="Ieškoti pagal pavadinimą"
              className="form-control me-2"
              size="small"
            />
            <TextField
              onChange={(b) => {setSearchBuilding(b.target.value); setPageNumber(0); setPage(1);}}
              value={searchBuinding}
              id="search-name-input"
              label="Ieškoti pagal pastatą"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={fectchRooms}
            >
              Ieškoti
            </button>
          </form>
        </div>
      </div>
      {/* <div className="d-flex justify-content-end">
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
            count={rooms.totalPages}
            defaultPage={1}
            siblingCount={0}
            onChange={handlePageChange}
            value={page}
          />
        </div>
      </div> */}

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Pavadinimas</th>
            <th>Pastatas</th>
            <th>Būsena</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {rooms.content?.map((room) => (
            <tr key={room.id} id={room.id}>
              <td>{room.name}</td>
              <td>{room.building}</td>
              <td>{room.deleted ? "Ištrintas" : ""}</td>
              <td className="text-end">
                <button className="btn btn-outline-primary me-1 my-1 btn-link" title="Žiūrėti">
                  <Link className="nav-link" to={"/rooms/view/" + room.id}>
                  <VisibilityTwoToneIcon/>
                  </Link>
                </button>
                <button
                  className="btn btn-outline-primary me-1 my-1 btn-link" title="Redaguoti"
                  disabled={room.deleted}
                >
                  <Link className="nav-link" to={"/rooms/edit/" + room.id}>
                  <EditTwoToneIcon/>
                  </Link>
                </button>

                {room.deleted ? (
                  <button
                  className="btn btn-outline-secondary me-1 my-1 btn-link" title="Atstatyti"
                    onClick={() => restoreRoom(room.id)}
                  >
                    <RestoreTwoToneIcon/>
                  </button>
                ) : (
                  <button
                  className="btn btn-danger me-2 my-1 btn-link" title="Ištrinti"
                    onClick={() => deleteRoom(room.id)}
                  >
                    <DeleteTwoToneIcon className="red-icon" />
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot className="table-light">
          <tr>
            <td>
              {rooms.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${rooms.totalElements}`}
            </td>
            <td></td>
            <td></td>
            <td></td>
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
            count={rooms.totalPages}
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

export default RoomListPage;
