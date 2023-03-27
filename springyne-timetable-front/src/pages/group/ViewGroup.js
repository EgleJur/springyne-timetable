import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";
import { apiUrl } from "../../App";

function ViewGroupPage() {
  const [group, setGroup] = useState({});
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/groups/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`${apiUrl}/api/v1/groups/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
             }, 5000);
  };

  const handleRestore = () => {
    fetch(`${apiUrl}/api/v1/groups/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setRestored(true);
    setDeleted(false);
    setTimeout(() => {
      setRestored(false);
             }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Grupė</h2>
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
              <td>{group.name}</td>
            </tr>
            <tr>
              <th scope="col">Mokslo metai</th>
              <td>{group.groupYear}</td>
            </tr>
            <tr>
              <th scope="col">Studentų skaičius</th>
              <td>{group.students}</td>
            </tr>
            <tr>
              <th scope="col">Programa</th>
              <td>{group.program?.name}</td>
            </tr>
            <tr>
              <th scope="col">Pamaina</th>
              <td>{group.shift?.name}</td>
            </tr>
            <tr>
              <th scope="col">Būsena</th>
              <td>{group.deleted ? "Ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas</th>
              <td>{group.modifiedDate}</td>
            </tr>
          </tbody>
        </table>

        <button className="btn btn-primary me-2 mb-5" disabled={group.deleted}>
          <Link className="nav-link" to={"/groups/edit/" + group.id}>
            Redaguoti
          </Link>
        </button>

        {group.deleted ? (
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

export default ViewGroupPage;
