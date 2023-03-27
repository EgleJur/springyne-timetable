import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";
import { apiUrl } from "../../App";

function ViewProgramPage() {
  const [program, setProgram] = useState({});
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/programs/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setProgram(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`${apiUrl}/api/v1/programs/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setProgram(jsonResponse));
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
             }, 5000);
  };

  const handleRestore = () => {
    fetch(`${apiUrl}/api/v1/programs/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setProgram(jsonResponse));
    setRestored(true);
    setDeleted(false);
    setTimeout(() => {
      setRestored(false);
             }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Programa</h2>

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
              <td colSpan={4}>{program.name}</td>
            </tr>
            <tr>
              <th scope="col">Aprašymas</th>
              <td colSpan={4}>{program.description}</td>
            </tr>
            <tr>
              <th scope="col">Būsena</th>
              <td colSpan={4}>{program.deleted ? "Ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas</th>
              <td colSpan={4}>{program.modifiedDate}</td>
            </tr>
            <tr>
              <th rowSpan="0">Dalykai</th>
            </tr>
            {program.subjects?.map((subject) => (
              <tr
                className={subject.subject.deleted ? "text-black-50" : ""}
                key={subject.subject.id}
              >
                <td>{subject.subject.name}</td>
                <td>{subject.hours} valandų</td>
                {subject.subject.deleted ? <td>Ištrintas</td> : <td></td>}
              </tr>
            ))}
          </tbody>
        </table>
        <button
          className="btn btn-primary me-2 mb-5"
          disabled={program.deleted}
        >
          <Link to={"/programs/edit/" + params.id} className="nav-link">
            Redaguoti
          </Link>
        </button>
        {program.deleted ? (
          <button
            className="btn btn-secondary me-2 mb-5"
            onClick={handleRestore}
          >
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2 mb-5" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </div>
    </div>
  );
}

export default ViewProgramPage;
