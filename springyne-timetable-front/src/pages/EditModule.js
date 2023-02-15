import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditModulePage() {
  const [module, setModule] = useState({});
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [changed, setChanged] = useState(false);
  const params = useParams();

  const fetchModule = () => {
    fetch("/api/v1/modules/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
  };

  useEffect(fetchModule, []);

  const editModule = (e) => {
    e.preventDefault();
    setNumberError(false);
    setNameError(false);
    if (module.name === "" || module.number === "") {
      if (module.number === "") {
        setNumberError(true);
      }
      if (module.name === "") {
        setNameError(true);
      }
    } else {
      fetch("/api/v1/modules/update/" + params.id, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(module),
      }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          setChanged(false);
          fetchModule();
        } else {
          setFailure(true);
          setSuccess(false);
          setNumberError(true);
          setNameError(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setModule({
      ...module,
      [property]: event.target.value,
    });
    setChanged(true);
  };

  const handleDelete = () => {
    fetch(`/api/v1/modules/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNumberError(false);
    setNameError(false);
    setChanged(false);
  };

  const handleRestore = () => {
    fetch(`/api/v1/modules/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNumberError(false);
    setNameError(false);
    setChanged(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti modulį</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
        </Alert>
      </Collapse>
      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>
      <form noValidate>
        <table
          className="table table-hover shadow p-3 mb-5 bg-body rounded
        align-middle"
        >
          <tbody>
            <tr>
              <th scope="col">
                <label htmlFor="edit-module-number-with-error">Numeris *</label>
              </th>
              <td>
                <TextField
                  error={!!numberError}
                  onChange={(e) => updateProperty("number", e)}
                  value={module.number}
                  id="edit-module-number-with-error"
                  helperText="Numeris turi būti unikalus ir negali būti tuščias"
                  className="form-control"
                  size="small"
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>
            <tr>
              <th scope="col">
                <label htmlFor="edit-module-name-with-error">
                  Pavadinimas *
                </label>
              </th>
              <td>
                <TextField
                  error={!!nameError}
                  onChange={(e) => updateProperty("name", e)}
                  value={module.name}
                  id="edit-module-number-with-error"
                  helperText="Pavadinimas negali būti tuščias"
                  className="form-control"
                  size="small"
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>
            <tr>
              <th scope="col">Detalės</th>
              <td>{module.deleted ? "Modulis ištrintas" : ""}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas:</th>
              <td>{module.modifiedDate}</td>
            </tr>
          </tbody>
        </table>
        <button type="submit" className="btn btn-primary me-2" onClick={editModule} disabled={!changed}>
          Redaguoti
        </button>
        {module.deleted ? (
          <button className="btn btn-secondary me-2" onClick={handleRestore}>
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </form>
    </div>
  );
}

export default EditModulePage;
