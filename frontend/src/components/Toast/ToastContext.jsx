import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
} from "react";
import styles from "./Toast.module.css";

const ToastContext = createContext(null);

let idSeq = 1;

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const remove = useCallback((id) => {
    setToasts((list) => list.filter((t) => t.id !== id));
  }, []);

  const addToast = useCallback(
    (message, opts = {}) => {
      const id = idSeq++;
      const duration = opts.duration ?? 2500;
      setToasts((list) => [...list, { id, message }]);
      if (duration > 0) {
        setTimeout(() => remove(id), duration);
      }
      return id;
    },
    [remove]
  );

  const value = useMemo(() => ({ addToast, remove }), [addToast, remove]);

  return (
    <ToastContext.Provider value={value}>
      {children}
      <div className={styles.stack} aria-live="polite" aria-atomic="true">
        {toasts.map((t) => (
          <div key={t.id} className={styles.toast} role="status">
            {t.message}
            <button
              className={styles.close}
              onClick={() => remove(t.id)}
              aria-label="Zatvori"
            >
              ×
            </button>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
}

export const useToast = () => useContext(ToastContext);
