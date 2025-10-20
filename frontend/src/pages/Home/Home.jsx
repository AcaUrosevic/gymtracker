import Container from "../../components/Container/Container";
import Button from "../../components/Button/Button";
import Card from "../../components/Card/Card";
import styles from "./Home.module.css";

// === SLIKE KOJE IMAŠ ===
import heroImg from "../../assets/images/hero.png";
import featureRecordsImg from "../../assets/images/feature-records.png";
import featureMembersImg from "../../assets/images/feature-members.png";
import featureTrainersImg from "../../assets/images/feature-trainers.png";

export default function Home() {
  return (
    <>
      <section className={styles.hero}>
        <Container>
          <div className={styles.heroGrid}>
            <div className={styles.heroText}>
              <h1 className={styles.title}>Sve za teretanu na jednom mestu</h1>
              <p className={styles.lead}>
                Upravljaj trenerima, članovima i evidencijama treninga — brzo,
                jasno i bez papirologije.
              </p>
              <div className={styles.actions}>
                <Button as="a" href="/trainers">
                  Lista trenera
                </Button>
                <Button as="a" href="/members" variant="ghost">
                  Lista članova
                </Button>
              </div>
            </div>

            <div className={styles.heroImage} aria-label="glavna ilustracija">
              <img
                src={heroImg}
                alt="Digitalna ilustracija: menadžment teretane u tamnom stilu sajta"
                className={styles.heroImgEl}
                loading="eager"
                decoding="async"
              />
            </div>
          </div>
        </Container>
      </section>

      <section className={styles.features}>
        <Container>
          <div className={styles.grid}>
            <Card
              title="Evidencije treninga"
              subtitle="Kreiranje, pretraga i izmena"
            >
              <div className={styles.featureImageWrap}>
                <img
                  src={featureRecordsImg}
                  alt=""
                  className={styles.featureImg}
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <p>
                Vodi evidenciju po treneru, članu, vežbi i datumu. Dodaj stavke
                sa serijama, ponavljanjima i težinama — sve na jednom mestu.
              </p>
            </Card>

            <Card title="Članovi" subtitle="Sve informacije na klik">
              <div className={styles.featureImageWrap}>
                <img
                  src={featureMembersImg}
                  alt=""
                  className={styles.featureImg}
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <p>
                Podaci o članstvu, kontakt, paketi usluga i istorija treninga u
                jasnom prikazu.
              </p>
            </Card>

            <Card title="Treneri" subtitle="Tim na dlanu">
              <div className={styles.featureImageWrap}>
                <img
                  src={featureTrainersImg}
                  alt=""
                  className={styles.featureImg}
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <p>
                Sertifikati trenera pod kontrolom. Brzo dodaj, ažuriraj i prati
                akreditacije trenera.
              </p>
            </Card>
          </div>
        </Container>
      </section>

      <section className={styles.cta}>
        <Container>
          <div className={styles.ctaCard}>
            <h2>Spreman za organizovanije treninge?</h2>
            <p>Započni sa evidencijama i planiranjem u par klikova.</p>

            <div className={styles.actions}>
              <Button as="a" href="/training-records">
                Otvori evidencije
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
