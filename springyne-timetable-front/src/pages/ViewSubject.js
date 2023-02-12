import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";

function ViewSubjectPage() {
  const [subject, setSubject] = useState([]);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/subjects/view/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`/api/v1/subjects/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setDeleted(true);
    setRestored(false);
  };

  const handleRestore = () => {
    fetch(`/api/v1/subjects/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setRestored(true);
    setDeleted(false);
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
              <th scope="col">Moduliai</th>
              <td>{subject.module?.map((mod) => (
                <p key={mod.id} id={mod.id}>{mod.name}</p>
              ))}</td>
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
              <th scope="col">Paskutinį kartą modifikuotas:</th>
              <td>{subject.lastUpdated}</td>
            </tr>
          </tbody>
        </table>
        <button className="btn btn-primary me-2">
          <Link to={"/subjects/edit/" + params.id} className="nav-link">
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
