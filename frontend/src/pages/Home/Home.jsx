import Container from "../../components/Container/Container";
import Button from "../../components/Button/Button";
import Card from "../../components/Card/Card";
import styles from "./Home.module.css";

export default function Home() {
  return (
    <>
      <section className={styles.hero}>
        <Container>
          <div className={styles.heroGrid}>
            <div className={styles.heroText}>
              <h1 className={styles.title}>Sve za teretanu na jednom mestu</h1>
              <p className={styles.lead}>
                Upravljaj trenerima, članovima i rasporedima — brzo, jasno i bez
                papirologije.
              </p>
              <div className={styles.actions}>
                <Button as="a" href="/trainers">
                  Pogledaj trenere
                </Button>
                <Button as="a" href="/members" variant="ghost">
                  Lista članova
                </Button>
              </div>
            </div>

            <div
              className={styles.heroImage}
              aria-label="hero image placeholder"
            >
              <span>Ovde ubaciti glavnu sliku (1920×1080)</span>
            </div>
          </div>
        </Container>
      </section>

      <section className={styles.features}>
        <Container>
          <div className={styles.grid}>
            <Card
              title="Planiranje treninga"
              subtitle="Brzo zakazivanje i pregled"
            >
              <p>
                Kreiraj termine, dodeli trenere i prati popunjenost dvorane.
              </p>
            </Card>
            <Card title="Evidencija članova" subtitle="Sve informacije na klik">
              <p>
                Podaci o članstvu, status uplate, kontakt i istorija dolazaka.
              </p>
            </Card>
            <Card title="Sertifikati" subtitle="Upravljanje akreditacijama">
              <p>Dodaj, pretraži i ažuriraj sertifikate trenera.</p>
            </Card>
          </div>
        </Container>
      </section>

      <section className={styles.strip}>
        <Container>
          <div className={styles.stripGrid}>
            <div className={styles.imgPh}>
              <span>Slika #1 (4:3)</span>
            </div>
            <div className={styles.imgPh}>
              <span>Slika #2 (4:3)</span>
            </div>
            <div className={styles.imgPh}>
              <span>Slika #3 (4:3)</span>
            </div>
          </div>
        </Container>
      </section>

      <section className={styles.cta}>
        <Container>
          <div className={styles.ctaCard}>
            <h2>Spreman za organizovanije treninge?</h2>
            <p>Zapocni sa evidencijom i planiranjem u par klikova.</p>

            <div className={styles.actions}>
              <Button as="a" href="/training-records">
                Otvori raspored
              </Button>
              <Button as="a" href="/about" variant="ghost">
                Saznaj više
              </Button>
            </div>
          </div>
        </Container>
      </section>
    </>
  );
}
