import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";
import { apiUrl } from "../App";

function ViewSubjectPage() {
  const [subject, setSubject] = useState({});
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/subjects/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`${apiUrl}/api/v1/subjects/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
             }, 5000);
  };

  const handleRestore = () => {
    fetch(`${apiUrl}/api/v1/subjects/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setRestored(true);
    setDeleted(false);
    setTimeout(() => {
      setRestored(false);
             }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Dalykas</h2>
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
              <td>{subject.name}</td>
            </tr>
            <tr>
              <th scope="col">Aprašas</th>
              <td>{subject.description}</td>
            </tr>
            <tr>
              <th scope="col">Moduliai</th>
              <td>{subject.module?.name}</td>
            </tr>
            <tr>
              <th scope="col">Kabinetai</th>
              <td>{subject.rooms?.map((room) => (
                <p key={room.id} id={room.id}>{room.name}</p>
              ))}</td>
            </tr>
            <tr>
              <th scope="col">Būsena</th>
              <td>{subject.deleted ? "Ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas</th>
              <td>{subject.last_Updated}</td>
            </tr>
          </tbody>
        </table>

          <button
            className="btn btn-primary me-2" disabled={subject.deleted}>
            <Link className="nav-link" to={"/subjects/edit/" + subject.id}>
              Redaguoti
            </Link>
          </button>

        {subject.deleted ? (
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

export default ViewSubjectPage;
