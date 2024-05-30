import React, { useState } from "react";
import "./Modal.scss";

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (title: string, description: string) => void;
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, onSave }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleSave = () => {
    onSave(title, description);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>Add New Task</h2>
        <div className="modal-content">
          <label>
            Title:
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </label>
          <label>
            Description:
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            ></textarea>
          </label>
        </div>
        <div className="modal-actions">
          <button onClick={onClose} className="cancel-btn">Cancel</button>
          <button onClick={handleSave} className="save-btn">Save</button>
        </div>
      </div>
    </div>
  );
};

export default Modal;
