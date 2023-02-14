import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { useParams } from "react-router-dom";
import { ModulesForSubjects } from "../components/ModulesForSubjects";
import { RoomsForSubjects } from "../components/RoomsForSubjects";


function CreateSubjectPage() {
  const [description, setDescription] = useState("");
  const [name, setName] = useState("");
  const [module, setModule] = useState({});
  const [room, setRoom] = useState("");
  const [nameError, setNameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();


  const createNewSubject = (e) => {
    e.preventDefault();
    setNameError(false);
    if (name === "") {
      setNameError(true);
    } else {
      fetch("/api/v1/subjects/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          description,
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setDescription("");
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };


  const selectModules = (event) => {
    setModule(event.target.value)
  }
  const selectRoom = (event) => {
    setRoom(event.target.value)
  }

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują dalyką</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
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
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>
      <form noValidate>

        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-subject-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />

        <TextField
          onChange={(e) => setDescription(e.target.value)}
          value={description}
          id="create-subject-number-with-error"
          label="Aprašas"
          className="form-control mb-3"
        />
        <label htmlFor="page-size-select" className="mb-3">
          Modulis:
        </label>
        <ModulesForSubjects id={params.id} onModuleChange={selectModules}/>


        <label htmlFor="page-size-select" className="mb-3">
          Kabinetas:
        </label>
        <ModulesForSubjects id={params.id} onModuleChange={selectRoom}/>

        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewSubject}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateSubjectPage;
