import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";

function ViewRoomPage() {
  const [room, setRoom] = useState({});
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/rooms/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`/api/v1/rooms/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
             }, 5000);
  };

  const handleRestore = () => {
    fetch(`/api/v1/rooms/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setRoom(jsonResponse));
    setRestored(true);
    setDeleted(false);
    setTimeout(() => {
      setRestored(false);
             }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Kabinetas</h2>
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
      <div className="">
      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
          <tbody>
            <tr>
              <th scope="col">Pavadinimas</th>
              <td>{room.name}</td>
            </tr>
            <tr>
              <th scope="col">Pastatas</th>
              <td>{room.building}</td>
            </tr>
            <tr>
              <th scope="col">Aprašymas</th>
              <td>{room.description}</td>
            </tr>
            <tr>
              <th scope="col">Būsena</th>
              <td>{room.deleted ? "Ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas</th>
              <td>{room.lastModifiedDate}</td>
            </tr>
          </tbody>
        </table>
        <button className="btn btn-primary me-2">
          <Link to={"/rooms/edit/" + params.id} className="nav-link">
            Redaguoti
          </Link>
        </button>
        {room.deleted ? (
          <button className="btn btn-secondary me-2" onClick={handleRestore}>
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </div>
    </div>
  );
}

export default ViewRoomPage;
