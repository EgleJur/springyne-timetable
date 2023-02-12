import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditSubjectPage() {
  const [module, setModule] = useState({});
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/modules/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
  }, [params.id]);

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
        } else {
          setFailure(true);
          setSuccess(false);
          setNumberError(true);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setModule({
      ...module,
      [property]: event.target.value,
    });
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
        <TextField
          error={!!numberError}
          onChange={(e) => updateProperty("number", e)}
          value={module.number}
          id="create-module-number-with-error"
          label="Numeris"
          helperText="Numeris turi būti unikalus ir negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={module.name}
          id="create-module-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <button type="submit" className="btn btn-primary" onClick={editModule}>
          Redaguoti
        </button>
      </form>
    </div>
  );
}

export default EditSubjectPage;
