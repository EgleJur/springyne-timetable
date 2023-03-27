import { useEffect, useState } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { apiUrl } from "../../App";

function CreateRoomPage() {
  const [name, setName] = useState("");
  const [building, setBuilding] = useState("");
  const [description, setDescription] = useState("");
  const [nameError, setNameError] = useState("");
  const [buildingError, setBuildingError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);



  const createNewRoom = (e) => {
    e.preventDefault();
    setNameError(false);
    setBuildingError(false);
    setDescription();
    if (name === "" || building === "") {
      if (name === "") {
        setNameError(true);
      }
      if (building === "") {
        setBuildingError(true);
      }
    } else {
      fetch(`${apiUrl}/api/v1/rooms/`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          building,
          description,
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setBuilding("");
          setDescription("");
          setSuccess(true);
          setFailure(false);
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      });
    }
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują kabinetą</h2>
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
          id="create-room-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!buildingError}
          onChange={(e) => setBuilding(e.target.value)}
          value={building}
          id="create-room-number-with-error"
          label="Pastatas"
          helperText="Pastatas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          // error={!!descriptionError}
          onChange={(e) => setDescription(e.target.value)}
          value={description}
          id="create-room-number-with-error"
          label="Aprašymas"
          helperText=""
          className="form-control mb-3"
          size="small"
          // required
        />
        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewRoom}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateRoomPage;
